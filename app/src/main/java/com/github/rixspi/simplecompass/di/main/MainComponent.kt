package com.github.rixspi.simplecompass.di.main

import com.github.rixspi.simplecompass.di.base.scope.ActivityScope
import com.github.rixspi.simplecompass.ui.main.MainActivity
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(MainModule::class)])
interface MainComponent {
    fun inject(mainActivity: MainActivity): MainActivity
}