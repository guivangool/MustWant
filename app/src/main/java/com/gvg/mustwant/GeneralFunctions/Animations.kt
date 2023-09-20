package com.gvg.mustwant.GeneralFunctions

import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

fun animateLogo(
    image: View?,
    durationLong:Long = 1500,
    fromX:Float = 0.4f,
    toX:Float = 1f,
    fromY:Float = 0.4f,
    toY:Float = 1f) {

    val scaleAnimation = ScaleAnimation(
        0.4f, 1f, 0.4f, 1f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )
    scaleAnimation.duration = durationLong

    image!!.startAnimation(scaleAnimation)

}

fun vibrateView(viewElement: View?)
{
    val animation = TranslateAnimation(0f, 5f, 0f, 5f) // Cambia el valor 10f según lo vibrante que desees que sea
    animation.duration = 1500 // Duración de la animación en milisegundos
    animation.interpolator = CycleInterpolator(5f) // Esto hará que la animación se repita 5 veces, creando una vibración

    viewElement!!.startAnimation(animation)
}