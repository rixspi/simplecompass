package com.github.rixspi.simplecompass.ui.compass

import android.databinding.ObservableInt
import com.github.rixspi.simplecompass.ui.base.BaseViewModel
import com.github.rixspi.simplecompass.util.compass.CompassManager
import javax.inject.Inject


class CompassViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var compassManager: CompassManager

    val currentAzimuth = ObservableInt()
    val lastAzimuth = ObservableInt()

    fun startCompass() {
        compassManager.registerSensorListener()
        configureCompassEventListener()
    }

    fun pauseCompass() {
        compassManager.unregisterSensorListener()
        compassManager.setOnCompassEventListener(null)
    }

    private fun configureCompassEventListener() {
        compassManager.setOnCompassEventListener { last, current ->
            lastAzimuth.set(last)
            currentAzimuth.set(current)
        }
    }

}