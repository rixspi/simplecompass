package com.github.rixspi.simplecompass.compass.adapters

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

class LocationListenerAdapter(val onLocationChange: (Location?) -> Unit) : LocationListener {
    override fun onLocationChanged(location: Location?) = onLocationChange(location)

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        //currently not in used, but required by the OS
    }

    override fun onProviderEnabled(p0: String?) {
        //currently not in used, but required by the OS
    }

    override fun onProviderDisabled(p0: String?) {
        //currently not in used, but required by the OS
    }
}