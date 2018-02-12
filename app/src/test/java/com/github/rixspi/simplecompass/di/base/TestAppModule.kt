package com.github.rixspi.simplecompass.di.base

import com.github.rixspi.simplecompass.util.compass.LocationValidator
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock


@Module
class TestAppModule {
    @Provides
    fun provideLocationValidator(): LocationValidator = mock(LocationValidator::class.java)
}