package com.github.rixspi.simplecompass.util.compass

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.os.Bundle

typealias CompassEventListener = (Int, Int) -> Unit

const val INVALID_LOCATION = -1

interface CompassManager : LocationListener, SensorEventListener {

    fun registerSensorListener(): Boolean

    fun unregisterSensorListener()

    fun setOnCompassEventListener(compassEventListener: CompassEventListener?)

    fun registerLocationChangesListener()

    fun unregisterLocationChangesListener()

    fun getBearingBetweenCurrentAnd(currentLocation: Location?, dest: Location?): Double

    fun getCurrentLocation(): Location?

    // no op functions

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}