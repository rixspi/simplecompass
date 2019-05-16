package com.github.rixspi.simplecompass.compass.providers

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.location.Location
import android.location.LocationManager
import com.github.rixspi.simplecompass.compass.adapters.LocationListenerAdapter
import javax.inject.Inject


class LocationProviderLiveData @Inject constructor(
        private val locationManager: LocationManager
) : LiveData<Location>() {

    private val locationListenerAdapter: LocationListenerAdapter = LocationListenerAdapter { value = it }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun registerLocationChangesListener() {
        //FIXME MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                0f, locationListenerAdapter)
    }

    private fun unregisterLocationChangesListener() {
        locationManager.removeUpdates(locationListenerAdapter)
    }


    override fun onActive() {
        super.onActive()
        postValue(getLastKnownLocation())
        registerLocationChangesListener()
    }

    override fun onInactive() {
        super.onInactive()
        unregisterLocationChangesListener()
    }
}

