package com.github.rixspi.simplecompass.compass.adapters

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class SensorEventListenerAdapter(private val onSensorChange: (SensorEvent) -> Unit) : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent) = onSensorChange(event)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //currently not used, but required by the OS
    }
}