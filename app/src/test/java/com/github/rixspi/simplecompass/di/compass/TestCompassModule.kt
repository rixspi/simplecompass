package com.github.rixspi.simplecompass.di.compass

import android.app.Activity
import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import com.github.rixspi.simplecompass.ui.compass.CompassViewAccess
import com.github.rixspi.simplecompass.ui.main.MainActivity
import com.github.rixspi.simplecompass.util.compass.CompassManager
import dagger.Module
import dagger.Provides
import org.mockito.Mockito


@FragmentScope
@Module
class TestCompassModule {
    @Provides
    fun provideCompassFragmentViewAccess(): CompassViewAccess = Mockito.mock(CompassViewAccess::class.java)

    @Provides
    fun provideParentActivity(): Activity = Mockito.mock(MainActivity::class.java)

    @Provides
    fun provideCompassManager(): CompassManager = Mockito.mock(CompassManager::class.java)
}