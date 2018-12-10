package com.github.rixspi.simplecompass.di.compass

import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.location.LocationManager
import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import com.github.rixspi.simplecompass.ui.compass.CompassFragment
import com.github.rixspi.simplecompass.ui.compass.CompassViewAccess
import com.github.rixspi.simplecompass.ui.location.LocationChooserFragment
import com.github.rixspi.simplecompass.ui.location.LocationChooserViewAccess
import com.github.rixspi.simplecompass.util.compass.CompassManager
import com.github.rixspi.simplecompass.util.compass.CompassManagerImpl
import dagger.Module
import dagger.Provides


@FragmentScope
@Module
class LocationChooserModule(private val fragment: LocationChooserFragment) {
    @Provides
    fun provideLocationChooserFragment(): LocationChooserFragment = fragment

    @Provides
    fun provideLocationChooserFragmentViewAccess(): LocationChooserViewAccess = fragment

    @Provides
    fun provideParentActivity(): Activity = fragment.activity

}