package com.github.rixspi.simplecompass.ui.location

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.rixspi.simplecompass.R
import com.github.rixspi.simplecompass.databinding.FragmentLocationChooserBinding
import com.github.rixspi.simplecompass.di.compass.LocationChooserModule
import com.github.rixspi.simplecompass.ui.base.BaseFragment
import javax.inject.Inject


//Change to the history of locations instead of choosing
class LocationChooserFragment : BaseFragment(), LocationChooserViewAccess {
    @Inject
    lateinit var viewModel: LocationChooserViewModel

    private lateinit var binding: FragmentLocationChooserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(LocationChooserModule(this)).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_chooser, null, false)
        binding.model = viewModel
        binding.viewAccess = this

        return binding.root
    }


}