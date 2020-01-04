package jp.bellware.echo.view.main

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import jp.bellware.echo.store.AnimationStatus

/**
 * メイン画面のアニメーションViewHelper
 */
class AnimatorViewHelper {
    fun apply(view: View, status: AnimationStatus?) {
        when (status) {
            AnimationStatus.FI1, AnimationStatus.FI2 -> {
                view.visibility = View.VISIBLE
                view.translationY = 0f
                val a = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f)
                a.duration = 500
                a.start()
            }
            AnimationStatus.DELETE -> {
                view.visibility = View.VISIBLE
                val dp = view.resources.displayMetrics.density
                val pvha = PropertyValuesHolder.ofFloat(View.ALPHA, view.alpha, 0f)
                val pvht = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, 100f * dp)
                val animator = ObjectAnimator.ofPropertyValuesHolder(view, pvha, pvht)
                animator.duration = 200
                animator.start()
            }
            AnimationStatus.VISIBLE -> {
                view.visibility = View.VISIBLE
                view.alpha = 1.0f
                view.translationY = 0.0f
            }
            AnimationStatus.INVISIBLE -> {
                view.visibility = View.INVISIBLE
                view.alpha = 1.0f
                view.translationY = 0.0f
            }
            null -> {

            }
        }
    }

}