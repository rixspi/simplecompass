package com.github.rixspi.simplecompass

import android.app.Application
import android.content.Context
import com.github.rixspi.simplecompass.di.base.AppComponent
import com.github.rixspi.simplecompass.di.base.AppModule
import com.github.rixspi.simplecompass.di.base.DaggerAppComponent


class CompassApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        fun get(context: Context) = context.applicationContext as CompassApplication
    }
}