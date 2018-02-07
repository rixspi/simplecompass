package com.github.rixspi.simplecompass.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.rixspi.simplecompass.R
import com.github.rixspi.simplecompass.databinding.ActivityMainBinding
import com.github.rixspi.simplecompass.di.main.MainModule
import com.github.rixspi.simplecompass.ui.base.BaseActivity
import com.github.rixspi.simplecompass.ui.compass.CompassFragment
import javax.inject.Inject


class MainActivity : BaseActivity(), MainViewAccess {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(MainModule(this)).inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.model = viewModel
        binding.viewAccess = this

        changeFragment(MainPosition.Compass)
    }

    private fun changeFragment(position: MainPosition) {
        val fragment: Fragment = when (position) {
            MainPosition.Compass -> CompassFragment()
        }

        supportFragmentManager.
                beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.flMainContainer, fragment)
                .commit()
    }
}