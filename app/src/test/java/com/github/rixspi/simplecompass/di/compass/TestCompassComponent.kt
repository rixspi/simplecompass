package com.github.rixspi.simplecompass.di.compass

import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [(TestCompassModule::class)])
interface TestCompassComponent {
    fun inject(test: CompassViewModelTest): CompassViewModelTest
}