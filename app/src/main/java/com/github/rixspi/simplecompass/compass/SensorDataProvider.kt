package com.github.rixspi.simplecompass.compass

import android.arch.lifecycle.LiveData
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.rixspi.simplecompass.compass.adapters.SensorEventListenerAdapter
import javax.inject.Inject


class SensorDataProviderLiveData @Inject constructor(
        private val sensorManager: SensorManager
) : LiveData<Pair<Boolean, SensorEvent>>() {

    private var rotationVectorSensorAvailable = true
    private val sensorEventListenerAdapter = SensorEventListenerAdapter { value = Pair(rotationVectorSensorAvailable, it) }

    override fun onActive() {
        super.onActive()
        registerSensorListener()
    }

    override fun onInactive() {
        super.onInactive()
        sensorManager.unregisterListener(sensorEventListenerAdapter)
    }

    fun registerSensorListener() {
        rotationVectorSensorAvailable = sensorManager.registerListener(sensorEventListenerAdapter,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_FASTEST)

        if (!rotationVectorSensorAvailable) {
            sensorManager.registerListener(sensorEventListenerAdapter, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)
            sensorManager.registerListener(sensorEventListenerAdapter, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

}
