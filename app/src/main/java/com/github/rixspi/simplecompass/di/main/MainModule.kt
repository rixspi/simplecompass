package com.github.rixspi.simplecompass.di.main

import com.github.rixspi.simplecompass.di.base.scope.ActivityScope
import com.github.rixspi.simplecompass.ui.main.MainActivity
import com.github.rixspi.simplecompass.ui.main.MainViewAccess
import dagger.Module
import dagger.Provides

@ActivityScope
@Module
class MainModule(private val activity: MainActivity) {
    @Provides
    fun provideMainActivity(): MainActivity = activity

    @Provides
    fun provideMainViewAccess(): MainViewAccess = activity
}