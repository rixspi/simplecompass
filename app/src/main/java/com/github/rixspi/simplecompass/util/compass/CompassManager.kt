package com.github.rixspi.simplecompass.util.compass

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.os.Bundle

typealias CompassEventListener = (Int, Int) -> Unit

const val INVALID_LOCATION = -1.0

const val DEFAULT_ORIENTATION_THRESHOLD = 0.9f

interface CompassManager : LocationListener, SensorEventListener {
    var orientationChangeThresholdInDegrees: Float

    fun registerSensorListener(): Boolean

    fun unregisterSensorListener()

    fun setOnCompassEventListener(compassEventListener: CompassEventListener?)

    fun setOrientationChangeThreshold(degrees: Float)

    fun registerLocationChangesListener()

    fun unregisterLocationChangesListener()

    fun getBearingBetweenCurrentAnd(dest: Location): Double


    // no op functions

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}