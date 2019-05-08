package com.github.rixspi.simplecompass.util.compass

import android.arch.lifecycle.*
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import com.github.rixspi.simplecompass.compass.LocationProviderLiveData
import com.github.rixspi.simplecompass.compass.SensorDataProviderLiveData
import com.github.rixspi.simplecompass.util.combineAndCompute
import java.util.*


class CompassManagerImpl(
        private val locationProvider: LocationProviderLiveData,
        private val sensorDataProvider: SensorDataProviderLiveData
) : CompassManager {

    override var destination: Location? = null
    private var orientation = FloatArray(3)
    private var rMat = FloatArray(9)

    private var currentDegree = 0F

    private var accelerometerData = FloatArray(3)
    private var magnetometerData = FloatArray(3)

    private val fullCircleDegrees: Int = 360
    private val halfCircleDegrees: Int = 180

    private val lowPassAlpha = 0.15f


    override fun getLiveData(): LiveData<CompassData> = sensorDataProvider.combineAndCompute(locationProvider) { sensorEvent: Pair<Boolean, SensorEvent>, location: Location? ->
        when {
            sensorEvent.first.not() -> calculateDegreesFromAccelAndMagneto(sensorEvent.second, location, calculateDeclination(location))
            sensorEvent.second.sensor.type == Sensor.TYPE_ROTATION_VECTOR -> calculateDegreesFromRotation(sensorEvent.second, location)
            else -> CompassData(0, 0, 0.0)
        }
    }


    private fun lowPassFilter(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input

        for (i in input.indices) {
            output[i] = output[i] + lowPassAlpha * (input[i] - output[i])
        }
        return output
    }

    private fun calculateDegreesFromAccelAndMagneto(event: SensorEvent, currentLocation: Location?, declination: Float): CompassData {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = lowPassFilter(event.values.clone(), accelerometerData)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerData = lowPassFilter(event.values.clone(), magnetometerData)
        }

        return if (SensorManager.getRotationMatrix(rMat, null, accelerometerData, magnetometerData)) {
            SensorManager.getOrientation(rMat, orientation)

            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            calculateAzimuthAndNotifyListeners(azimuth, currentLocation, declination)
        } else {
            CompassData(0, 0, 0.0)
        }
    }

    private fun calculateDegreesFromRotation(event: SensorEvent, currentLocation: Location?): CompassData {
        var azimuth = getAzimuthFromRotationMatrixAndOrientation(event)
        azimuth = transformDegreesToRotation(currentDegree, -azimuth)
        val compasEventListener = CompassData(currentDegree.toInt(), azimuth.toInt(), getBearingBetweenCurrentAnd(destination, currentLocation))
        currentDegree = azimuth

        return compasEventListener
    }

    private fun calculateAzimuthAndNotifyListeners(azimuth: Float, currentLocation: Location?, declination: Float): CompassData {
        val azimuthDegrees = transformDegreesToRotation(currentDegree, -(azimuth + declination))
        val compassData = CompassData(currentDegree.toInt(), azimuthDegrees.toInt(), getBearingBetweenCurrentAnd(destination, currentLocation))
        currentDegree = azimuthDegrees
        return compassData
    }

    private fun getAzimuthFromRotationMatrixAndOrientation(event: SensorEvent): Float {
        SensorManager.getRotationMatrixFromVector(rMat, event.values)
        return Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0].toDouble()).toFloat()
    }

    private fun getDifferenceBetweenDegrees(first: Float, second: Float): Float = Math.abs(first - second)

    /**
     * Helper method for calculating proper rotation value for use in the view's rotate animation
     */
    fun transformDegreesToRotation(lastDegree: Float, degree: Float): Float {
        return if (getDifferenceBetweenDegrees(lastDegree, degree) > halfCircleDegrees) {
            degree + if (lastDegree >= 0) fullCircleDegrees else -fullCircleDegrees
        } else {
            degree
        }
    }

    fun calculateDeclination(location: Location?): Float =
            if (location != null) {
                GeomagneticField(location.latitude.toFloat(),
                        location.longitude.toFloat(), location.altitude.toFloat(), Date().time)
                        .declination
            } else {
                0f
            }


    fun getBearingBetweenCurrentAnd(currentLocation: Location?, dest: Location?): Double =
            if (currentLocation != null && dest != null) {
                val bearing: Double = currentLocation.bearingTo(dest).toDouble()
                currentDegree + bearing
            } else {
                INVALID_LOCATION.toDouble()
            }
}