package com.github.rixspi.simplecompass.util.compass

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.github.rixspi.simplecompass.BaseTest
import com.github.rixspi.simplecompass.compass.CompassManagerImpl
import com.github.rixspi.simplecompass.compass.INVALID_LOCATION
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hamcrest.CoreMatchers.`is` as _is
import org.mockito.Mockito.`when` as _when


class CompassManagerTest : BaseTest() {
    lateinit var locationManager: LocationManager
    lateinit var sensorManager: SensorManager
    lateinit var compassManager: CompassManagerImpl

   //TODO Update tests
}