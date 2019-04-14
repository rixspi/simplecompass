package com.github.rixspi.simplecompass.util.compass

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.support.annotation.RequiresPermission
import com.github.rixspi.simplecompass.compass.LocationProvider
import com.github.rixspi.simplecompass.compass.SensorDataProvider
import com.github.rixspi.simplecompass.util.arrayOfNotNullOrNull
import java.util.*


class CompassManagerImpl(
        private val locationProvider: LocationProvider,
        private val sensorDataProvider: SensorDataProvider,
        private val lifecycle: Lifecycle)
    : CompassManager {

    override var destination: Location? = null
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

    init {
        lifecycle.addObserver(sensorDataProvider)
        lifecycle.addObserver(locationProvider)
        lifecycle.addObserver(this)

        sensorDataProvider.setOnSensorChangedEventListener { vectorRotationAvailable, event ->
            if (vectorRotationAvailable.not()) {
                calculateDegreesFromAccelAndMagneto(event)
            } else if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                calculateDegreesFromRotation(event)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun removeLifecycleObservers() = with(lifecycle) {
        removeObserver(sensorDataProvider)
        removeObserver(locationProvider)
        removeObserver(this@CompassManagerImpl)
    }

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

    private fun calculateDegreesFromAccelAndMagneto(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = lowPassFilter(event.values.clone(), accelerometerData)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerData = lowPassFilter(event.values.clone(), magnetometerData)
        }

        if (SensorManager.getRotationMatrix(rMat, null, accelerometerData, magnetometerData)) {
            SensorManager.getOrientation(rMat, orientation)

            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            calculateAzimuthAndNotifyListeners(azimuth)
        }
    }

    private fun calculateDegreesFromRotation(event: SensorEvent) {
        var azimuth = getAzimuthFromRotationMatrixAndOrientation(event)
        azimuth = transformDegreesToRotation(currentDegree, -azimuth)
        compassEventListener?.invoke(currentDegree.toInt(), azimuth.toInt(), locationProvider.getBearingBetweenCurrentAnd(destination))
        currentDegree = azimuth
    }

    private fun calculateAzimuthAndNotifyListeners(azimuth: Float) {
        val azimuthDegrees = transformDegreesToRotation(currentDegree, -(azimuth + declination))
        compassEventListener?.invoke(currentDegree.toInt(), azimuthDegrees.toInt(), locationProvider.getBearingBetweenCurrentAnd(destination))
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
}