package com.github.guqt178.utils.anim


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils


/**
 * 淡出gone动画
 */
fun View.goneFade(duration: Number = 600L) {
    val target = this
    target.animate()
            .alpha(0f)
            .setDuration(duration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    target.visibility = View.GONE
                }
            })
}

/**
 * 淡入show动画
 */
fun View.showFade(duration: Number = 600L) {

    this.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        alpha = 0f
        visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
                .alpha(1f)
                .setDuration(duration.toLong())
                .setListener(null)
    }
}


//揭露show动画
fun View?.showReveal() {
    if (this == null) return

    // Check if the runtime version is at least Lollipop
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // get the center for the clipping circle
        val cx = width / 2
        val cy = height / 2

        // get the final radius for the clipping circle
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius)
        // make the view visible and start the animation
        this.visibility = View.VISIBLE
        anim.start()
    } else {
        // set the view to invisible without a circular reveal animation below Lollipop
        this.visibility = View.VISIBLE
    }

}

//揭露gone动画
fun View?.goneReveal() {

    val target = this ?: return

    // Check if the runtime version is at least Lollipop
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // get the center for the clipping circle
        val cx = target.width / 2
        val cy = target.height / 2

        // get the initial radius for the clipping circle
        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animation (the final radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(target, cx, cy, initialRadius, 0f)

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                target.visibility = View.INVISIBLE
            }
        })

        // start the animation
        anim.start()
    } else {
        // set the view to visible without a circular reveal animation below Lollipop
        target.visibility = View.INVISIBLE
    }
}