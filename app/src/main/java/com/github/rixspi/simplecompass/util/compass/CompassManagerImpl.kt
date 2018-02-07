package com.github.rixspi.simplecompass.util.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class CompassManagerImpl(private val sensorManager: SensorManager,
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

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //no op
    }
}