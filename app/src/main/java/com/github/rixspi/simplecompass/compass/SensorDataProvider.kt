package com.github.rixspi.simplecompass.compass

import android.arch.lifecycle.LifecycleObserver
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.rixspi.simplecompass.compass.adapters.SensorEventListenerAdapter

interface SensorDataProvider : SensorEventListenerAdapter {

}

class SensorDataProviderImpl(private val sensorManager: SensorManager) : SensorDataProvider, LifecycleObserver {
    override fun onSensorChanged(event: SensorEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}