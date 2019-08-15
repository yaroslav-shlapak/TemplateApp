package com.company.app.presentation.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewPropertyAnimator


/**
 * Created on 2018/10/18.
 */
const val DEFAULT_ANIMATION_DURATION = 100L

inline fun View?.fade(
    animationDuration: Long = DEFAULT_ANIMATION_DURATION,
    crossinline andThen: () -> Unit
): ViewPropertyAnimator? {
    this?.apply {
        alpha = 1f
        visibility = View.VISIBLE
        return animate()
            .alpha(0f)
            .setDuration(animationDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = View.GONE
                    andThen()
                }
            })
    }
    return null
}

inline fun View?.appear(
    animationDuration: Long = DEFAULT_ANIMATION_DURATION,
    crossinline andThen: () -> Unit
): ViewPropertyAnimator? {
    this?.apply {
        alpha = 0f
        visibility = View.VISIBLE
        return animate()
            .alpha(1f)
            .setDuration(animationDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    andThen()
                }
            })
    }
    return null
}