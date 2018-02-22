package com.vovasoft.unilot.components

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator


/***************************************************************************
 * Created by arseniy on 03/02/2018.
 ****************************************************************************/
object AnimationUtils {

    fun registerCircularRevealAnimation(context: Context?, view: View, revealSettings: RevealAnimationSetting) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context?.let { context ->
                view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        v.removeOnLayoutChangeListener(this)
                        val cx = revealSettings.centerX
                        val cy = revealSettings.centerY
                        val width = revealSettings.width
                        val height = revealSettings.height
                        val duration = context.resources.getInteger(android.R.integer.config_longAnimTime)

                        //Simply use the diagonal of the view
                        val finalRadius = Math.sqrt((width * width + height * height)).toFloat()
                        val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius).setDuration(duration.toLong())
                        anim.interpolator = AccelerateDecelerateInterpolator()
                        anim.start()
                    }
                })
            }
        }
    }
}
