package com.github.rixspi.simplecompass.ui.main

import com.github.rixspi.simplecompass.BaseTest
import com.github.rixspi.simplecompass.di.base.DaggerTestAppComponent
import com.github.rixspi.simplecompass.di.main.TestMainModule
import org.junit.Before


class MainViewModelTest : BaseTest() {
    @Before
    fun setUp() {
        DaggerTestAppComponent.builder()
                .build()
                .plus(TestMainModule())
                .inject(this)
    }

    //Nothing to test yet :(
}
