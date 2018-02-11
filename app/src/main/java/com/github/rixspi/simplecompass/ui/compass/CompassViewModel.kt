package com.github.rixspi.simplecompass.ui.compass

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.location.Location
import com.github.rixspi.simplecompass.ui.base.BaseViewModel
import com.github.rixspi.simplecompass.util.compass.CompassManager
import com.github.rixspi.simplecompass.util.compass.INVALID_LOCATION
import javax.inject.Inject

class CompassViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var compassManager: CompassManager

    val currentAzimuth = ObservableInt()
    val lastAzimuth = ObservableInt()

    val latitude = ObservableField<String>("")
    val longitude = ObservableField<String>("")

    private var destination: Location? = null

    val destinationHeading = ObservableInt(INVALID_LOCATION)

    fun startCompass() {
        compassManager.registerSensorListener()
        configureCompassEventListener()
    }

    fun pauseCompass() {
        compassManager.unregisterSensorListener()
        compassManager.setOnCompassEventListener(null)
        compassManager.unregisterLocationChangesListener()
    }

    fun acceptCoordinates() {
        if (latitude.get().isEmpty() or longitude.get().isEmpty()) {
            destination = null
        } else {
            destination = Location("").apply {
                latitude = this@CompassViewModel.latitude.get().toDouble()
                longitude = this@CompassViewModel.longitude.get().toDouble()
            }
        }
    }


    private fun configureCompassEventListener() {
        compassManager.setOnCompassEventListener { last, current ->
            lastAzimuth.set(last)
            currentAzimuth.set(current)
            destination?.let {
                destinationHeading.set(compassManager.getBearingBetweenCurrentAnd(compassManager.getCurrentLocation(), it).toInt())
            }

        }
    }

}