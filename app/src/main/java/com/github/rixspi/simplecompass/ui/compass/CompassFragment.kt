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
import javax.inject.Inject


class CompassFragment : BaseFragment(), CompassViewAccess {
    private lateinit var binding: FragmentCompassBinding

    @Inject
    lateinit var viewModel: CompassViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(CompassModule(this)).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compass, null, false)
        binding.model = viewModel
        binding.viewAccess = this

        return binding.root
    }
}