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

    override fun registerSensorListener() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI)
    }

    override fun unregisterSensorListener() {
        sensorManager.unregisterListener(this)
    }

    override fun setOnCompassEventListener(compassEventListener: CompassEventListener?) {
        this.compassEventListener = compassEventListener
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values)
            var azimuth: Float = Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0].toDouble()).toFloat()
            azimuth = cleanDegrees(-azimuth)

            val difference = Math.abs(currentDegree - azimuth)
            if (difference > orientationChangeThresholdInDegrees) {
                compassEventListener?.invoke(currentDegree.toInt(), azimuth.toInt())
                currentDegree = azimuth
            }
        }
    }

    /**
     * Helper method for calculating proper rotation value for use in the view's rotate animation
     */
    private fun cleanDegrees(degree: Float): Float {
        val difference = Math.abs(currentDegree - degree)
        return if (difference > halfCircleDegrees) {
            degree + if (currentDegree >= 0) fullCircleDegrees else -fullCircleDegrees
        } else {
            degree
        }
    }

    /**
     * Calculates the bearing between two locations with the current heading of the phone.
     * With this function you can make a view that points on the destination location if you give
     * the current location as the first parameter. You can choose whether the result should be
     * smoothed or not.
     * @param lat1 the latitude of the first location. This could be the current phones location
     * @param lng1 the longitude of the first location. This could be the current phones location
     * @param lat2 the latitude of the destination location at which to point at.
     * @param lng2 the longitude of the destination location at which to point at.
     * @return degrees including the current heading of the phone.
     */
    fun getBearingBetweenCurrentAnd(dest: Location): Double {
        currentLocation?.let {
            val x = Math.cos(dest.latitude) * Math.sin(it.longitude - dest.longitude)
            val y = Math.cos(it.latitude) * Math.sin(dest.latitude) - Math.sin(it.latitude) * Math.cos(dest.latitude) * Math.cos(it.longitude - dest.longitude)
            val bearing = Math.atan2(x, y)
            return -this.currentDegree + Math.toDegrees(bearing)
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