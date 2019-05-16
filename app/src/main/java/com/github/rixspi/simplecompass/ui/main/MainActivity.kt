package com.github.rixspi.simplecompass.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.rixspi.simplecompass.R
import com.github.rixspi.simplecompass.databinding.Main

import com.github.rixspi.simplecompass.di.main.MainModule
import com.github.rixspi.simplecompass.ui.base.BaseActivity
import com.github.rixspi.simplecompass.ui.compass.CompassFragment
import com.github.rixspi.simplecompass.ui.location.LocationChooserFragment
import com.github.rixspi.simplecompass.util.enlargeThenShrink
import javax.inject.Inject


class MainActivity : BaseActivity(), MainViewAccess {
    private lateinit var binding: Main

    @Inject
    lateinit var viewModel: MainViewModel

    private fun changeFragment(position: MainPosition) {
        val fragment: Fragment = when (position) {
            MainPosition.Compass -> CompassFragment()
            MainPosition.Locations -> LocationChooserFragment()
        }

        supportFragmentManager.
                beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.flMainContainer, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(MainModule(this)).inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.model = viewModel
        binding.viewAccess = this

        changeFragment(MainPosition.Compass)
    }

    override fun goToCompass() {
        binding.tvCompass.enlargeThenShrink { changeFragment(MainPosition.Compass) }

    }

    override fun goToLocations() {
        binding.tvLocations.enlargeThenShrink { changeFragment(MainPosition.Locations) }
    }
}