package com.example.musthave.GeneralFunctions

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

fun animateLogo(image: View?, durationLong:Long = 1500) {

    val scaleAnimation = ScaleAnimation(
        0.4f, 1f, 0.4f, 1f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )
    scaleAnimation.duration = durationLong

    image!!.startAnimation(scaleAnimation)

}