package com.github.rixspi.simplecompass.ui.base

import android.databinding.BaseObservable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class BaseViewModel : BaseObservable() {
    private val disposables = CompositeDisposable()

    fun registerDisposable(disposable: Disposable) = disposables.add(disposable)

    fun clearDisposables() = disposables.clear()
}