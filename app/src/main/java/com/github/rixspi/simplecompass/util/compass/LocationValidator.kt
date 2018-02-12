package com.github.rixspi.simplecompass.util.compass


const val LAT_MAX_DEGREE = 90
const val LNG_MAX_DEGREE = 180

interface LocationValidator {
    fun validateLongitude(lng: Double): Boolean

    fun validateLatitude(lng: Double): Boolean
}