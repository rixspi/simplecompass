package com.github.rixspi.simplecompass.util.compass

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.location.LocationListener
import android.os.Bundle
import android.support.annotation.RequiresPermission
import java.util.jar.Manifest

typealias CompassEventListener = (Int, Int) -> Unit
typealias LocationEventListener = (Int, Int) -> Unit

const val INVALID_LOCATION = -1.0

const val DEFAULT_ORIENTATION_THRESHOLD = 0.9f

interface CompassManager : LocationListener, SensorEventListener {
    var orientationChangeThresholdInDegrees: Float

    fun registerSensorListener()

    fun unregisterSensorListener()

    fun setOnCompassEventListener(compassEventListener: CompassEventListener?)

    fun setOrientationChangeThreshold(degrees: Float)

    fun registerLocationChangesListener()

    fun unregisterLocationChangesListener()


    // no op functions

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}