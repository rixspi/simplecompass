package com.github.rixspi.simplecompass.di.compass

import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import com.github.rixspi.simplecompass.ui.compass.CompassFragment
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [(CompassModule::class)])
interface CompassComponent {
    fun inject(compassFragment: CompassFragment): CompassFragment
}