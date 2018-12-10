package com.github.rixspi.simplecompass.util

import android.view.View
import io.reactivex.Completable


private const val ANIMATION_DURATION_MILISECONDS = 150L
private const val DEFAULT_SCALE_VALUE = 1.0f
private const val ENLARGED_SCALE_VALUE = 1.1f
const val ENLARGED_BIG_SCALE_VALUE = 1.3f

fun View.enlarge(scale: Float = ENLARGED_SCALE_VALUE, duration: Long = ANIMATION_DURATION_MILISECONDS) =
        Completable.create {
            animate()
                    .scaleX(scale)
                    .scaleY(scale)
                    .setDuration(duration)
                    .withEndAction(it::onComplete)
        }


fun View.shrink(scale: Float = DEFAULT_SCALE_VALUE, duration: Long = ANIMATION_DURATION_MILISECONDS) =
        Completable.create {
            animate()
                    .scaleX(scale)
                    .scaleY(scale)
                    .setDuration(duration)
                    .withEndAction(it::onComplete)
        }

fun View.enlargeThenShrink(end: () -> Unit = {}) {
    this.enlarge().andThen(this.shrink())
            .doOnComplete { end() }
            .subscribe()
}
