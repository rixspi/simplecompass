package com.github.rixspi.simplecompass.util.compass

import android.arch.lifecycle.LifecycleObserver
import android.location.Location

typealias CompassEventListener = (Int, Int, Double) -> Unit

const val INVALID_LOCATION = -10000

interface CompassManager : LifecycleObserver {
    var destination: Location?

    fun setOnCompassEventListener(compassEventListener: CompassEventListener?)
}