package com.github.rixspi.simplecompass

import okhttp3.ResponseBody
import org.junit.Rule
import org.mockito.Mockito
import retrofit2.Response


open class BaseTest {
    @Suppress("unused")
    @get:Rule
    val rxJavaRule: RxJavaTestRule = RxJavaTestRule()

    fun response(success: Boolean): Response<Void> =
            if (success) {
                Response.success(null)
            } else {
                Response.error<Void>(400, Mockito.mock(ResponseBody::class.java))
            }

    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T
}