package com.github.rixspi.simplecompass.compass

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.location.Location

const val INVALID_LOCATION = -10000

interface CompassManager : LifecycleObserver {
    var destination: Location?

    fun getLiveData(): LiveData<CompassData>
}