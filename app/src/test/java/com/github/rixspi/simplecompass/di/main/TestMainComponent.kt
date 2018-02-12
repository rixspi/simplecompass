package com.github.rixspi.simplecompass.di.main

import com.github.rixspi.simplecompass.di.base.scope.ActivityScope
import com.github.rixspi.simplecompass.ui.main.MainViewModelTest
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(TestMainModule::class)])
interface TestMainComponent {
    fun inject(test: MainViewModelTest): MainViewModelTest
}