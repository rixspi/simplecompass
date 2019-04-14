package com.github.rixspi.simplecompass.util.compass

import android.location.Location
import com.github.rixspi.simplecompass.compass.adapters.LocationListenerAdapter
import com.github.rixspi.simplecompass.compass.adapters.SensorEventListenerAdapter

typealias CompassEventListener = (Int, Int) -> Unit

const val INVALID_LOCATION = -10000

interface CompassManager : LocationListenerAdapter, SensorEventListenerAdapter {

    fun registerSensorListener(): Boolean

    fun unregisterSensorListener()

    fun setOnCompassEventListener(compassEventListener: CompassEventListener?)

    fun registerLocationChangesListener()

    fun unregisterLocationChangesListener()

    fun getBearingBetweenCurrentAnd(currentLocation: Location?, dest: Location?): Double

    fun getCurrentLocation(): Location?
}