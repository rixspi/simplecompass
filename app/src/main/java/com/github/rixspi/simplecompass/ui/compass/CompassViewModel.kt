package com.github.rixspi.simplecompass.ui.compass

import android.databinding.ObservableInt
import android.location.Location
import com.github.rixspi.simplecompass.ui.base.BaseViewModel
import com.github.rixspi.simplecompass.util.compass.CompassManager
import javax.inject.Inject

const val INVALID_DESTINATION_HEADING = -10000

class CompassViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var compassManager: CompassManager

    val currentAzimuth = ObservableInt()
    val lastAzimuth = ObservableInt()

    val destinationHeading = ObservableInt(INVALID_DESTINATION_HEADING)

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
//            destinationHeading.set(compassManager.getBearingBetweenCurrentAnd(Location("").apply {
//                latitude = 48.864716
//                longitude = 2.349014
//            }).toInt())
        }
    }

}