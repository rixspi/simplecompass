package com.github.rixspi.simplecompass.compass.math

import android.hardware.GeomagneticField
import android.location.Location
import com.github.rixspi.simplecompass.compass.INVALID_LOCATION
import org.xml.sax.helpers.LocatorImpl
import java.util.*

private const val fullCircleDegrees: Int = 360
private const val halfCircleDegrees: Int = 180

private const val lowPassAlpha = 0.15f

private fun getDifferenceBetweenDegrees(first: Float, second: Float): Float = Math.abs(first - second)

internal fun lowPassFilter(input: FloatArray, output: FloatArray?): FloatArray {
    if (output == null) return input

    for (i in input.indices) {
        output[i] = output[i] + lowPassAlpha * (input[i] - output[i])
    }
    return output
}

internal fun transformDegreesToRotation(lastDegree: Float, degree: Float): Float {
    return if (getDifferenceBetweenDegrees(lastDegree, degree) > halfCircleDegrees) {
        degree + if (lastDegree >= 0) fullCircleDegrees else -fullCircleDegrees
    } else {
        degree
    }
}

fun Location?.calculateDeclination(): Float =
        if (this != null) {
            GeomagneticField(this.latitude.toFloat(),
                    this.longitude.toFloat(), this.altitude.toFloat(), Date().time)
                    .declination
        } else {
            0f
        }


fun Location?.bearingBetween(dest: Location?, currentDegree: Float): Double =
        if (this != null && dest != null) {
            val bearing: Double = this.bearingTo(dest).toDouble()
            currentDegree + bearing
        } else {
            INVALID_LOCATION.toDouble()
        }