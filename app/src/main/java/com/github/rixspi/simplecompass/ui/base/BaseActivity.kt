package com.github.rixspi.simplecompass.ui.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.github.rixspi.simplecompass.CompassApplication

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    val appComponent by lazy {
        CompassApplication.get(applicationContext).appComponent
    }

    fun hideKeyboard() {
        currentFocus?.let {
            it.clearFocus()
            val manager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    fun showSnackbar(view: View, @StringRes message: Int, delay: Int = Snackbar.LENGTH_LONG,
                     actionId: Int, runnable: (View) -> Unit) =
            Snackbar.make(view, message, delay).setAction(actionId, runnable).show()

    fun showSnackbar(view: View, @StringRes message: Int, delay: Int = Snackbar.LENGTH_SHORT) =
            Snackbar.make(view, message, delay).show()

    val connectivityBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isOnline()) {
                onOnlineModeEnabled()
            } else {
                onOfflineModeEnabled()
            }
        }
    }

    fun isOnline(): Boolean =
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetworkInfo?.isConnected
                    ?: false

    fun onOnlineModeEnabled() {}

    fun onOfflineModeEnabled() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(connectivityBroadcastReceiver, IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        unregisterReceiver(connectivityBroadcastReceiver)
        super.onDestroy()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments
                .filter { it is IOnBackPress }
                .firstOrNull { (it as IOnBackPress).onBackPressed() }
                ?.let { return }
        super.onBackPressed()
    }
}
