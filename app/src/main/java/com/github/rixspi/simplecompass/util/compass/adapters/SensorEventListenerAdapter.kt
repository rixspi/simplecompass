package com.github.rixspi.simplecompass.util.compass.adapters

import android.hardware.Sensor
import android.hardware.SensorEventListener

interface SensorEventListenerAdapter : SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //currently not in used, but required by the OS
    }
}