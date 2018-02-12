package com.github.rixspi.simplecompass.ui.compass

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.location.Location
import com.github.rixspi.simplecompass.ui.base.BaseViewModel
import com.github.rixspi.simplecompass.util.compass.CompassManager
import com.github.rixspi.simplecompass.util.compass.INVALID_LOCATION
import com.github.rixspi.simplecompass.util.compass.LocationValidator
import javax.inject.Inject

class CompassViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var compassManager: CompassManager

    @Inject
    lateinit var viewAccess: CompassViewAccess

    @Inject
    lateinit var locationValidator: LocationValidator

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
        if (isInputLocationProvided()) {
            handleInvalidLocation()
            updateDestinationWithProvidedLocation()
        } else {
            destination = null
            destinationHeading.set(INVALID_LOCATION)
        }

        viewAccess.hideKeyboard()
    }

    private fun isInputLocationProvided(): Boolean = latitude.get().isNotEmpty() and longitude.get().isNotEmpty()

    private fun updateDestinationWithProvidedLocation() {
        destination = Location("").apply {
            latitude = this@CompassViewModel.latitude.get().toDouble()
            longitude = this@CompassViewModel.longitude.get().toDouble()
        }
    }

    private fun handleInvalidLocation() {
        val isLatValid = locationValidator.validateLatitude(latitude.get().toDouble())
        val isLngValid = locationValidator.validateLongitude(longitude.get().toDouble())

        viewAccess.handleInvalidLatError(show = !isLatValid)
        viewAccess.handleInvalidLngError(show = !isLngValid)
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