package com.github.rixspi.simplecompass.ui.compass

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.rixspi.simplecompass.R
import com.github.rixspi.simplecompass.databinding.FragmentCompassBinding
import com.github.rixspi.simplecompass.di.compass.CompassModule
import com.github.rixspi.simplecompass.ui.base.BaseFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.jar.Manifest
import javax.inject.Inject


class CompassFragment : BaseFragment(), CompassViewAccess {
    private lateinit var binding: FragmentCompassBinding

    @Inject
    lateinit var viewModel: CompassViewModel

    private val rxPermissions: RxPermissions by lazy {
        RxPermissions(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(CompassModule(this)).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compass, null, false)
        binding.model = viewModel
        binding.viewAccess = this

        rxPermissions.request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({
                    if(it) {
                        viewModel.compassManager.registerLocationChangesListener()
                    }
                })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.startCompass()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseCompass()
        viewModel.compassManager.unregisterLocationChangesListener()
    }
}