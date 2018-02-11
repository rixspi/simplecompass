package com.github.rixspi.simplecompass.di.main

import com.github.rixspi.simplecompass.di.base.scope.ActivityScope
import com.github.rixspi.simplecompass.ui.main.MainViewAccess
import dagger.Module
import dagger.Provides
import org.mockito.Mockito


@Module
class TestMainModule {
    @Provides
    @ActivityScope
    fun provideViewAccess(): MainViewAccess = Mockito.mock(MainViewAccess::class.java)
}