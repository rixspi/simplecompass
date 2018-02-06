package com.github.rixspi.simplecompass.di.compass

import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import com.github.rixspi.simplecompass.ui.compass.CompassFragment
import com.github.rixspi.simplecompass.ui.compass.CompassViewAccess
import dagger.Module
import dagger.Provides


@FragmentScope
@Module
class CompassModule(private val fragment: CompassFragment) {
    @Provides
    fun provideCompassFragment(): CompassFragment = fragment

    @Provides
    fun provideCompassFragmentViewAccess(): CompassViewAccess = fragment
}