package com.github.rixspi.simplecompass.compass

import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.hardware.GeomagneticField
import android.location.Location
import android.location.LocationManager
import com.github.rixspi.simplecompass.util.arrayOfNotNullOrNull
import com.github.rixspi.simplecompass.util.compass.INVALID_LOCATION
import com.github.rixspi.simplecompass.compass.adapters.LocationListenerAdapter
import java.util.*

interface LocationProvider: LifecycleObserver {
    var currentDegree: Float
    var declination: Float

    fun registerLocationChangesListener()

    fun unregisterLocationChangesListener()

    fun getBearingBetweenCurrentAnd(dest: Location?): Double

    fun getCurrentLocation(): Location?
}

class LocationProviderImpl(private val locationManager: LocationManager) : LocationProvider, LocationListenerAdapter {
    override var currentDegree: Float = 0F

    override var declination: Float = 0F
    private var currentLocation: Location? = null


    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun registerLocationChangesListener() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                0f, this)

        onLocationChanged(getLastKnownLocation())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun unregisterLocationChangesListener() {
        locationManager.removeUpdates(this)
    }

    override fun getBearingBetweenCurrentAnd(dest: Location?): Double {
        arrayOfNotNullOrNull(currentLocation, dest)?.let { (current, dest) ->
            val bearing: Double = current.bearingTo(dest).toDouble()
            return this.currentDegree + bearing
        } ?: run {
            return INVALID_LOCATION.toDouble()
        }
    }

    override fun getCurrentLocation(): Location? = currentLocation

    override fun onLocationChanged(location: Location?) {
        this.currentLocation = location

        currentLocation?.let {
            declination = GeomagneticField(it.latitude.toFloat(),
                    it.longitude.toFloat(), it.altitude.toFloat(), Date().time)
                    .declination
        }
    }
}
