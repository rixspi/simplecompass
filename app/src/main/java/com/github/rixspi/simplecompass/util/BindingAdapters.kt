package com.github.rixspi.simplecompass.util

import android.databinding.BindingAdapter
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation


@BindingAdapter("visibleIf")
fun changeVisibility(view: View?, visible: Boolean) {
    view?.let { it.visibility = if (visible) View.VISIBLE else View.GONE }
}

@BindingAdapter(value = ["animateRotationFrom", "animateRotationTo"])
fun animateRotation(view: View?, from: Int?, to: Int?) {
    view?.let {
        val ra = RotateAnimation(
                from!!.toFloat(),
                to!!.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        ra.duration = 10
        ra.fillAfter = true

        it.startAnimation(ra)
    }
}


