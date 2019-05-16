package com.github.rixspi.simplecompass.di.compass

import android.app.Activity
import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import com.github.rixspi.simplecompass.ui.location.LocationChooserFragment
import com.github.rixspi.simplecompass.ui.location.LocationChooserViewAccess
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