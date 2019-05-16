package com.github.rixspi.simplecompass.compass

import android.arch.lifecycle.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import com.github.rixspi.simplecompass.compass.math.*
import com.github.rixspi.simplecompass.compass.math.calculateDeclination
import com.github.rixspi.simplecompass.compass.math.lowPassFilter
import com.github.rixspi.simplecompass.compass.math.transformDegreesToRotation
import com.github.rixspi.simplecompass.compass.providers.LocationProviderLiveData
import com.github.rixspi.simplecompass.compass.providers.SensorDataProviderLiveData
import com.github.rixspi.simplecompass.util.combineAndCompute


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


    override fun getLiveData(): LiveData<CompassData> = sensorDataProvider.combineAndCompute(locationProvider) { sensorEvent: Pair<Boolean, SensorEvent>, location: Location? ->
        when {
            sensorEvent.first.not() -> calculateDegreesFromAccelAndMagneto(sensorEvent.second, location, location.calculateDeclination())
            sensorEvent.second.sensor.type == Sensor.TYPE_ROTATION_VECTOR -> calculateDegreesFromRotation(sensorEvent.second, location)
            else -> CompassData(0, 0, 0.0)
        }
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
            calculateDegreesFromBackupSensors(azimuth, currentLocation, declination)
        } else {
            CompassData(0, 0, 0.0)
        }
    }

    private fun calculateDegreesFromRotation(event: SensorEvent, currentLocation: Location?): CompassData {
        val azimuthFromMatrix = getAzimuthFromRotationMatrixAndOrientation(event)
        val azimuth = transformDegreesToRotation(currentDegree, -azimuthFromMatrix)
        val bearing = currentLocation.bearingBetween(destination, currentDegree)
        return CompassData(
                degree = currentDegree.toInt(),
                azimuth = azimuth.toInt(),
                bearing = bearing
        ).also { currentDegree = azimuth }
    }

    private fun calculateDegreesFromBackupSensors(azimuth: Float, currentLocation: Location?, declination: Float): CompassData {
        val azimuthDegrees = transformDegreesToRotation(currentDegree, -(azimuth + declination))
        val bearing = currentLocation.bearingBetween(destination, currentDegree)
        return CompassData(
                degree = currentDegree.toInt(),
                azimuth = azimuthDegrees.toInt(),
                bearing = bearing
        ).also { currentDegree = azimuthDegrees }
    }

    private fun getAzimuthFromRotationMatrixAndOrientation(event: SensorEvent): Float {
        SensorManager.getRotationMatrixFromVector(rMat, event.values)
        return Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0].toDouble()).toFloat()
    }

}