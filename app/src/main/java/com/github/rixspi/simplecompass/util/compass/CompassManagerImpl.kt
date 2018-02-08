package com.github.rixspi.simplecompass.util.compass

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.support.annotation.RequiresPermission


class CompassManagerImpl(context: Activity,
                         override var orientationChangeThresholdInDegrees: Float = DEFAULT_ORIENTATION_THRESHOLD)
    : CompassManager {

    private var currentDegree: Float = 0f
    private var orientation = FloatArray(3)
    private var rMat = FloatArray(9)

    private var compassEventListener: CompassEventListener? = null

    private val fullCircleDegrees: Int = 360
    private val halfCircleDegrees: Int = 180

    private var currentLocation: Location? = null

    override fun setOrientationChangeThreshold(degrees: Float) {
        orientationChangeThresholdInDegrees = degrees
    }

    private var sensorManager: SensorManager = context.getSystemService(Service.SENSOR_SERVICE) as SensorManager
    private var locationManager: LocationManager = context.getSystemService(Service.LOCATION_SERVICE) as LocationManager


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
            SensorManager.getRotationMatrixFromVector(rMat, event.values)
            var azimuth: Float = Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0].toDouble()).toFloat()
            azimuth = transformDegreesToRotation(-azimuth)

//            if (getDifferenceBetweenDegrees(currentDegree, -azimuth) > orientationChangeThresholdInDegrees) {
                compassEventListener?.invoke(currentDegree.toInt(), azimuth.toInt())
//            }
            currentDegree = azimuth
        }
    }

    private fun getDifferenceBetweenDegrees(first: Float, second: Float): Float = Math.abs(first - second)

    /**
     * Helper method for calculating proper rotation value for use in the view's rotate animation
     */
    private fun transformDegreesToRotation(degree: Float): Float {
        return if (getDifferenceBetweenDegrees(currentDegree, degree) > halfCircleDegrees) {
            degree + if (currentDegree >= 0) fullCircleDegrees else -fullCircleDegrees
        } else {
            degree
        }
    }

    override fun getBearingBetweenCurrentAnd(dest: Location): Double {
        currentLocation?.let {
            val bearing: Double = currentLocation?.bearingTo(dest)?.toDouble() ?: 0.0
            return this.currentDegree + bearing
        } ?: run {
            return INVALID_LOCATION
        }
    }

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