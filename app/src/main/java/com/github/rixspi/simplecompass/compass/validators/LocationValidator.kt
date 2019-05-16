package com.github.rixspi.simplecompass.compass.validators


const val LAT_MAX_DEGREE = 90
const val LNG_MAX_DEGREE = 180

interface LocationValidator {
    fun validateLongitude(lng: Double): Boolean

    fun validateLatitude(lat: Double): Boolean
}