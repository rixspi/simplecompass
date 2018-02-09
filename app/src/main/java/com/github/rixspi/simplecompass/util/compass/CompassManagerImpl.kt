package com.github.rixspi.simplecompass.util.compass

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.support.annotation.RequiresPermission
import com.github.rixspi.simplecompass.util.arrayOfNotNullOrNull


class CompassManagerImpl(private val sensorManager: SensorManager, private val locationManager: LocationManager)
    : CompassManager {

    private var currentDegree: Float = 0f
    private var orientation = FloatArray(3)
    private var rMat = FloatArray(9)

    private var compassEventListener: CompassEventListener? = null

    private val fullCircleDegrees: Int = 360
    private val halfCircleDegrees: Int = 180

    private var currentLocation: Location? = null

    //TODO handle another sesnors if this isn't available
    override fun registerSensorListener() =
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                    SensorManager.SENSOR_DELAY_UI)


    override fun unregisterSensorListener() = sensorManager.unregisterListener(this)

    override fun setOnCompassEventListener(compassEventListener: CompassEventListener?) {
        this.compassEventListener = compassEventListener
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            handleDegreeCalculationFromSensorEvent(event)
        }
    }

    private fun handleDegreeCalculationFromSensorEvent(event: SensorEvent) {
        var azimuth = getAzimuthFromRotationMatrixAndOrientation(event)
        azimuth = transformDegreesToRotation(currentDegree, -azimuth)
        compassEventListener?.invoke(currentDegree.toInt(), azimuth.toInt())
        currentDegree = azimuth
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
            return INVALID_LOCATION
        }
    }

    fun getCurrentLocation(): Location? = currentLocation

    override fun onLocationChanged(location: Location) {
        this.currentLocation = location
    }

    override fun unregisterLocationChangesListener() {
        locationManager.removeUpdates(this)
    }


    @SuppressLint("MissingPermission")
    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    override fun registerLocationChangesListener() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L,
                50f, this)

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }
}