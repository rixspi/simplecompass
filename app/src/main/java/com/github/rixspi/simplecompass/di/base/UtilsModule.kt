package com.github.rixspi.simplecompass.di.base

import com.github.rixspi.simplecompass.di.base.scope.ActivityScope
import com.github.rixspi.simplecompass.util.compass.CompassManager
import com.github.rixspi.simplecompass.util.compass.CompassManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@ActivityScope
class UtilsModule {

    @Provides
    @Singleton
    fun provideCompassManager(): CompassManager = CompassManagerImpl()
}