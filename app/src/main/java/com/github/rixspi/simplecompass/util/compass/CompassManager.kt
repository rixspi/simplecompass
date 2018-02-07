package com.github.rixspi.simplecompass.util.compass

typealias CompassEventListener = (Int, Int) -> Unit
const val DEFAULT_ORIENTATION_THRESHOLD = 0.9f

interface CompassManager {
    var orientationChangeThresholdInDegrees: Float

    fun registerSensorListener()

    fun unregisterSensorListener()

    fun setOnCompassEventListener(compassEventListener: CompassEventListener?)

    fun setOrientationChangeThreshold(degrees: Float)
}