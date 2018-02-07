package com.github.rixspi.simplecompass.util.compass

import android.app.Activity
import android.app.Service
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager


class CompassManagerImpl(private val context: Activity,
                         override var orientationChangeThresholdInDegrees: Float = DEFAULT_ORIENTATION_THRESHOLD)
    : CompassManager, SensorEventListener {

    private var currentDegree: Float = 0f
    private var orientation = FloatArray(3)
    private var rMat = FloatArray(9)

    private var compassEventListener: CompassEventListener? = null

    private val fullCircleDegrees: Int = 360
    private val halfCircleDegrees: Int = 180

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
    fun getBearingBetweenLocations(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val x = Math.cos(lat2) * Math.sin(lng1-lng2)
        val y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lng1-lng2)
        val bearing = Math.atan2(x, y)
        return -this.currentDegree + Math.toDegrees(bearing)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //no op
    }
}