package com.github.rixspi.simplecompass.compass.adapters

import android.hardware.Sensor
import android.hardware.SensorEventListener

interface SensorEventListenerAdapter : SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //currently not used, but required by the OS
    }
}