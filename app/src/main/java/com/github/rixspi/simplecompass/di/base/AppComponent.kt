package com.github.rixspi.simplecompass.di.base

import com.github.rixspi.simplecompass.di.compass.CompassComponent
import com.github.rixspi.simplecompass.di.compass.CompassModule
import com.github.rixspi.simplecompass.di.compass.LocationChooseComponent
import com.github.rixspi.simplecompass.di.compass.LocationChooserModule
import com.github.rixspi.simplecompass.di.main.MainComponent
import com.github.rixspi.simplecompass.di.main.MainModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (UtilsModule::class)])
interface AppComponent {
    fun plus(module: MainModule): MainComponent

    fun plus(module: CompassModule): CompassComponent

    fun plus(module: LocationChooserModule): LocationChooseComponent
}