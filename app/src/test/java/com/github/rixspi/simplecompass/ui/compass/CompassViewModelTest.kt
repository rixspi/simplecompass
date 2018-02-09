package com.github.rixspi.simplecompass.ui.compass

import com.github.rixspi.simplecompass.BaseTest
import com.github.rixspi.simplecompass.di.base.DaggerTestAppComponent
import com.github.rixspi.simplecompass.di.compass.TestCompassModule
import com.github.rixspi.simplecompass.util.compass.CompassManager
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hamcrest.CoreMatchers.`is` as _is
import org.mockito.Mockito.`when` as _when


class CompassViewModelTest : BaseTest() {
    @Inject
    lateinit var viewModel: CompassViewModel

    @Before
    fun setUp() {
        DaggerTestAppComponent.builder()
                .build()
                .plus(TestCompassModule())
                .inject(this)
    }


    @Test
    fun `test compass start`() {
        viewModel.startCompass()

        verify(viewModel.compassManager).registerSensorListener()
        verify(viewModel.compassManager).setOnCompassEventListener(any())
    }

    @Test
    fun `verify compass pause unregisters listeners`() {
        viewModel.pauseCompass()

        verify(viewModel.compassManager).unregisterLocationChangesListener()
        verify(viewModel.compassManager).setOnCompassEventListener(null)
        verify(viewModel.compassManager).unregisterSensorListener()
    }
}