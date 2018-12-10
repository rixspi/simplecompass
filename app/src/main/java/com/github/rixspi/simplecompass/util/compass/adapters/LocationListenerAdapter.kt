package com.github.rixspi.simplecompass.util.compass.adapters

import android.location.LocationListener
import android.os.Bundle

interface LocationListenerAdapter : LocationListener {
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