package com.github.rixspi.simplecompass.compass

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.rixspi.simplecompass.compass.adapters.SensorEventListenerAdapter

typealias OnSensorChangedEvent = (Boolean, SensorEvent) -> Unit

interface SensorDataProvider : SensorEventListenerAdapter, LifecycleObserver {
    fun registerSensorListener(): Boolean

    fun setOnSensorChangedEventListener(listener: OnSensorChangedEvent)

    fun unregisterSensorListener()
}

class SensorDataProviderImpl(
        private val sensorManager: SensorManager
) : SensorDataProvider {
    private lateinit var onSensorChangedListener: OnSensorChangedEvent

    override fun setOnSensorChangedEventListener(listener: OnSensorChangedEvent) {
        onSensorChangedListener = listener
    }

    private var rotationVectorSensorAvailable: Boolean = true

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun registerSensorListener(): Boolean {
        rotationVectorSensorAvailable = sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_FASTEST)

        if (!rotationVectorSensorAvailable) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST)
        }

        return rotationVectorSensorAvailable
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun unregisterSensorListener() = sensorManager.unregisterListener(this)

    override fun onSensorChanged(event: SensorEvent) =
            onSensorChangedListener.invoke(rotationVectorSensorAvailable, event)
}