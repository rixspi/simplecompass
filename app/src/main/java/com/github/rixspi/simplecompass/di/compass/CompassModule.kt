package com.github.rixspi.simplecompass.di.compass

import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.location.LocationManager
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
    fun provideParentActivity(): Activity = fragment.activity

    @Provides
    fun provideSensorManager(): SensorManager =
            fragment.activity.getSystemService(SENSOR_SERVICE) as SensorManager

    @Provides
    fun provideLocationManager(): LocationManager =
            fragment.activity.getSystemService(LOCATION_SERVICE) as LocationManager

    @Provides
    fun provideCompassManager(sensorManager: SensorManager, locationManager: LocationManager): CompassManager =
            CompassManagerImpl(sensorManager, locationManager)
}