package com.github.rixspi.simplecompass.di.compass

import android.app.Service
import android.hardware.SensorManager
import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import com.github.rixspi.simplecompass.ui.compass.CompassFragment
import com.github.rixspi.simplecompass.ui.compass.CompassViewAccess
import com.github.rixspi.simplecompass.util.compass.CompassManager
import com.github.rixspi.simplecompass.util.compass.CompassManagerImpl
import dagger.Module
import dagger.Provides


@FragmentScope
@Module
class CompassModule(private val fragment: CompassFragment) {
    @Provides
    fun provideCompassFragment(): CompassFragment = fragment

    @Provides
    fun provideCompassFragmentViewAccess(): CompassViewAccess = fragment

    @Provides
    fun provideSensorManager(fragment: CompassFragment): SensorManager = fragment.activity.getSystemService(Service.SENSOR_SERVICE) as SensorManager

    @Provides
    fun provideCompassManager(sensorManager: SensorManager): CompassManager = CompassManagerImpl(sensorManager)
}