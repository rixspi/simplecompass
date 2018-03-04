package com.github.rixspi.simplecompass.ui.compass


interface CompassViewAccess {
    fun handleInvalidLatError(show: Boolean = true)

    fun handleInvalidLngError(show: Boolean = true)

    fun openGooglePlacePicker()

    fun hideKeyboard()
}