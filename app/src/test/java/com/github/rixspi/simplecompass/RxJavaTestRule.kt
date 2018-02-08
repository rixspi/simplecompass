package com.github.rixspi.simplecompass

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxJavaTestRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement = object : Statement() {
        override fun evaluate() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler({ Schedulers.trampoline() })
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            base?.evaluate()
        }
    }
}