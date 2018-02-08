package com.github.rixspi.simplecompass.ui.compass

import com.github.rixspi.simplecompass.BaseTest
import com.github.rixspi.simplecompass.di.base.DaggerTestAppComponent
import com.github.rixspi.simplecompass.di.compass.TestCompassModule
import org.junit.Before


class CompassViewModelTest : BaseTest() {
    @Before
    fun setUp() {
        DaggerTestAppComponent.builder()
                .build()
                .plus(TestCompassModule())
                .inject(this)
    }

}