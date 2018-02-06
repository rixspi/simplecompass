package com.github.rixspi.simplecompass.ui.main

import android.os.Bundle
import com.github.rixspi.simplecompass.di.main.MainModule
import com.github.rixspi.simplecompass.ui.base.BaseActivity


class MainActivity : BaseActivity(), MainViewAccess {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.plus(MainModule(this)).inject(this)


//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.profileModel = profileModel
//        binding.viewAccess = this
    }
}