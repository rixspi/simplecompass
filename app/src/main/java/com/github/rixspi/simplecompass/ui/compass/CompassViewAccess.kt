package com.github.rixspi.simplecompass.ui.compass

import android.arch.lifecycle.LifecycleOwner


interface CompassViewAccess {
    fun handleInvalidLatError(show: Boolean = true)

    fun handleInvalidLngError(show: Boolean = true)

    fun openGooglePlacePicker()

    fun getLifeCycleOwner(): LifecycleOwner

    fun hideKeyboard()
}