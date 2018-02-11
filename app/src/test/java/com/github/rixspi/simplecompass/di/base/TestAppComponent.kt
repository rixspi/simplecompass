package com.github.rixspi.simplecompass.di.base

import com.github.rixspi.simplecompass.di.compass.TestCompassComponent
import com.github.rixspi.simplecompass.di.compass.TestCompassModule
import com.github.rixspi.simplecompass.di.main.TestMainComponent
import com.github.rixspi.simplecompass.di.main.TestMainModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(TestAppModule::class)])
interface TestAppComponent {
    fun plus(module: TestCompassModule): TestCompassComponent

    fun plus(module: TestMainModule): TestMainComponent
}