package com.github.rixspi.simplecompass.util.compass

import android.annotation.SuppressLint
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.support.annotation.RequiresPermission
import com.github.rixspi.simplecompass.util.arrayOfNotNullOrNull
import java.util.*


class CompassManagerImpl(private val sensorManager: SensorManager, private val locationManager: LocationManager)
    : CompassManager {

    private var currentDegree: Float = 0f
    private var orientation = FloatArray(3)
    private var rMat = FloatArray(9)

    private var accelerometerData = FloatArray(3)
    private var magnetometerData = FloatArray(3)
    private var declination = 0.0f

    private var compassEventListener: CompassEventListener? = null

    private val fullCircleDegrees: Int = 360
    private val halfCircleDegrees: Int = 180

    private val lowPassAlpha = 0.15f

    private var currentLocation: Location? = null

    private var isRotationVectorSensorAvailable: Boolean = true

    override fun registerSensorListener(): Boolean {
        isRotationVectorSensorAvailable = sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI)

        if (!isRotationVectorSensorAvailable) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI)
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI)
        }

        return isRotationVectorSensorAvailable
    }

    override fun unregisterSensorListener() = sensorManager.unregisterListener(this)

    override fun setOnCompassEventListener(compassEventListener: CompassEventListener?) {
        this.compassEventListener = compassEventListener
    }


    private fun lowPassFilter(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input

        for (i in input.indices) {
            output[i] = output[i] + lowPassAlpha * (input[i] - output[i])
        }
        return output
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (!isRotationVectorSensorAvailable) {
            calculateDegreesFromAccelAndMagneto(event)
        } else if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            calculateDegreesFromRotation(event)
        }
    }

    private fun calculateDegreesFromAccelAndMagneto(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = lowPassFilter(event.values.clone(), accelerometerData)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerData = lowPassFilter(event.values.clone(), magnetometerData)
        }

        if (SensorManager.getRotationMatrix(rMat, null, accelerometerData, magnetometerData)) {
            SensorManager.getOrientation(rMat, orientation)

            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            calculateAzimuthAndInformListeners(azimuth)
        }
    }

    private fun calculateDegreesFromRotation(event: SensorEvent) {
        var azimuth = getAzimuthFromRotationMatrixAndOrientation(event)
        azimuth = transformDegreesToRotation(currentDegree, -azimuth)
        compassEventListener?.invoke(currentDegree.toInt(), azimuth.toInt())
        currentDegree = azimuth
    }

    private fun calculateAzimuthAndInformListeners(azimuth: Float) {
        val azimuthDegrees = transformDegreesToRotation(currentDegree, -(azimuth + declination))
        compassEventListener?.invoke(currentDegree.toInt(), azimuthDegrees.toInt())
        currentDegree = azimuthDegrees
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

    override fun getBearingBetweenCurrentAnd(currentLocation: Location?, dest: Location?): Double {
        arrayOfNotNullOrNull(currentLocation, dest)?.let { (current, dest) ->
            val bearing: Double = current.bearingTo(dest).toDouble()
            return this.currentDegree + bearing
        } ?: run {
            return INVALID_LOCATION.toDouble()
        }
    }

    override fun getCurrentLocation(): Location? = currentLocation

    override fun onLocationChanged(location: Location?) {
        this.currentLocation = location

        currentLocation?.let {
            declination = GeomagneticField(it.latitude.toFloat(),
                    it.longitude.toFloat(), it.altitude.toFloat(), Date().time)
                    .declination
        }
    }

    override fun unregisterLocationChangesListener() {
        locationManager.removeUpdates(this)
    }


    @SuppressLint("MissingPermission")
    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun registerLocationChangesListener() {
        locationManager.requestLocationUpdates(getLocationProvider(), 1000 * 60L,
                0f, this)

        onLocationChanged(locationManager.getLastKnownLocation(getLocationProvider()))
    }

    private fun getLocationProvider(): String {
        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
            isAltitudeRequired = false
            isBearingRequired = true
            isCostAllowed = true
            powerRequirement = Criteria.POWER_MEDIUM
        }

        return locationManager.getBestProvider(criteria, true)
    }
}