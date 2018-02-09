package com.github.rixspi.simplecompass.util.compass

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.github.rixspi.simplecompass.BaseTest
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

    @Before
    fun setUp() {
        locationManager = mock(LocationManager::class.java)
        sensorManager = mock(SensorManager::class.java)
        compassManager = CompassManagerImpl(locationManager = locationManager, sensorManager = sensorManager)
    }

    @Test
    fun `checks if request for location updates was called`() {
        compassManager.registerLocationChangesListener()
        verify(locationManager).requestLocationUpdates(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat(), any(LocationListener::class.java))
    }

    @Test
    fun `registering location updates listener initilizas the current position with last known`() {
        _when(locationManager.getLastKnownLocation(ArgumentMatchers.anyString())).thenReturn(mock(Location::class.java))
        compassManager.registerLocationChangesListener()

        assertTrue { compassManager.getCurrentLocation() != null }
    }

    @Test
    fun `checks if unregistering from location updates works`() {
        compassManager.unregisterLocationChangesListener()

        verify(locationManager).removeUpdates(any(LocationListener::class.java))
    }


    @Test
    fun `checks if request for sensor manager was called`() {
        val sensor = mock(Sensor::class.java)

        _when(sensorManager.getDefaultSensor(ArgumentMatchers.anyInt())).thenReturn(sensor)
        _when(sensorManager.registerListener(any(), any(Sensor::class.java), ArgumentMatchers.anyInt())).thenReturn(true)

        compassManager.registerSensorListener()

        verify(sensorManager).registerListener(any(), any(Sensor::class.java), ArgumentMatchers.anyInt())
    }

    @Test
    fun `checks if unregister from sesnor listener works`() {
        compassManager.unregisterSensorListener()

        verify(sensorManager).unregisterListener(any(SensorEventListener::class.java))
    }


    @Test
    fun `verify degree to rotation calculation`() {
        //This case is when user rotates between end and start of the cirle, so it's not jumping
        var rotation = compassManager.transformDegreesToRotation(0f, 360f)
        assertEquals( 720f ,  rotation)

        rotation = compassManager.transformDegreesToRotation(360f, 0f)
        assertEquals( 360f ,  rotation)

        //Normal case when the difference isn't bigger than 180
        rotation = compassManager.transformDegreesToRotation(6f, 0f)
        assertEquals( 0f ,  rotation)
    }

    @Test
    fun `verify if bearing calculation returns invalid while location is null`() {
        var bearingBetweenCurrentAnd = compassManager.getBearingBetweenCurrentAnd(null, mock(Location::class.java))
        assertEquals( INVALID_LOCATION, bearingBetweenCurrentAnd)

        bearingBetweenCurrentAnd = compassManager.getBearingBetweenCurrentAnd(mock(Location::class.java), null)
        assertEquals( INVALID_LOCATION, bearingBetweenCurrentAnd)
    }
}