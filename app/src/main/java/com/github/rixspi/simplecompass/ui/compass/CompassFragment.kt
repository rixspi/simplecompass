package com.github.rixspi.simplecompass.ui.compass

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.github.rixspi.simplecompass.R
import com.github.rixspi.simplecompass.databinding.FragmentCompassBinding
import com.github.rixspi.simplecompass.di.compass.CompassModule
import com.github.rixspi.simplecompass.ui.base.BaseFragment
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject


class CompassFragment : BaseFragment(), CompassViewAccess {
    private lateinit var binding: FragmentCompassBinding

    @Inject
    lateinit var viewModel: CompassViewModel


    private val rxPermissions: RxPermissions by lazy {
        RxPermissions(activity)
    }

    override fun handleInvalidLngError(show: Boolean) {
        handleInputErrorMessage(binding.inputLng, if (show) R.string.invalid_lng else null)
    }

    override fun handleInvalidLatError(show: Boolean) {
        handleInputErrorMessage(binding.inputLat, if (show) R.string.invalid_lat else null)
    }

    private fun handleInputErrorMessage(textInputLayout: TextInputLayout, messageStringId: Int?) {
        messageStringId?.let {
            viewModel.setDestinationHeadingInvalid()
            textInputLayout.error = getString(it)
        } ?: run {
            textInputLayout.error = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(CompassModule(this)).inject(this)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compass, null, false)
        binding.model = viewModel
        binding.viewAccess = this

        rxPermissions.request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({
                    viewModel.gpsPermitted = it
                })

        configureListeners()
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.startCompass()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseCompass()
    }

    private fun configureListeners() {
        binding.etLng.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.acceptCoordinates()
            }
            false
        }
    }
}