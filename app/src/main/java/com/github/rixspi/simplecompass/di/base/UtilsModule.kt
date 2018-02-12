package com.github.rixspi.simplecompass.di.base

import com.github.rixspi.simplecompass.di.base.scope.ActivityScope
import com.github.rixspi.simplecompass.util.compass.LocationValidator
import com.github.rixspi.simplecompass.util.compass.LocationValidatorImpl
import dagger.Module
import dagger.Provides

@Module
@ActivityScope
class UtilsModule {
    @Provides
    fun provideLocationValdator(): LocationValidator = LocationValidatorImpl()
}