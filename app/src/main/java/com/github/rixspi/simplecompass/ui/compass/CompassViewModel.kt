package com.github.rixspi.simplecompass.ui.compass

import android.arch.lifecycle.Observer
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.location.Location
import com.github.rixspi.simplecompass.ui.base.BaseViewModel
import com.github.rixspi.simplecompass.compass.CompassManager
import com.github.rixspi.simplecompass.compass.INVALID_LOCATION
import com.github.rixspi.simplecompass.compass.validators.LocationValidator
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

    var gpsPermitted = false
    val destinationHeading = ObservableInt(INVALID_LOCATION)

    fun dunno() {
        configureCompassEventListener()
    }

    fun acceptCoordinates() {
        if (isInputLocationProvided()) {
            handleInvalidLocation()
            updateDestinationWithProvidedLocation()
        } else {
            destination = null
            setDestinationHeadingInvalid()
        }

        viewAccess.hideKeyboard()
    }

    fun setDestinationHeadingInvalid() = destinationHeading.set(INVALID_LOCATION)

    private fun isInputLocationProvided(): Boolean = latitude.get()!!.isNotEmpty() and longitude.get()!!.isNotEmpty()

    private fun updateDestinationWithProvidedLocation() {
        destination = Location("").apply {
            latitude = this@CompassViewModel.latitude.get()!!.toDouble()
            longitude = this@CompassViewModel.longitude.get()!!.toDouble()
        }.also { compassManager.destination = it }
    }

    private fun handleInvalidLocation() {
        val isLatValid = locationValidator.validateLatitude(latitude.get()!!.toDouble())
        val isLngValid = locationValidator.validateLongitude(longitude.get()!!.toDouble())

        viewAccess.handleInvalidLatError(show = !isLatValid)
        viewAccess.handleInvalidLngError(show = !isLngValid)
    }

    private fun configureCompassEventListener() {
        compassManager.getLiveData().observe(viewAccess.getLifeCycleOwner(), Observer {
            it?.let { compassData ->
                lastAzimuth.set(compassData.azimuth)
                currentAzimuth.set(compassData.degree)
                destination?.let {
                    destinationHeading.set(compassData.bearing.toInt())
                }
            }
        })
    }

}