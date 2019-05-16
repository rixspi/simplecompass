package com.github.rixspi.simplecompass.compass.validators

import javax.inject.Inject


class LocationValidatorImpl @Inject constructor() : LocationValidator {

    override fun validateLatitude(lat: Double): Boolean {
        return lat >= -LAT_MAX_DEGREE && lat <= LAT_MAX_DEGREE
    }

    override fun validateLongitude(lng: Double): Boolean {
        return lng >= -LNG_MAX_DEGREE && lng <= LNG_MAX_DEGREE
    }
}