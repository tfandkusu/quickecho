package jp.bellware.echo.main

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

/**
 * メイン画面のアニメ隔離クラス
 */
class QRecAnimator {


    /**
     * 削除アニメーションを開始する。下への移動と
     */
    fun startDeleteAnimation(view: View) {
        val dp = view.resources.displayMetrics.density
        val pvha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val pvht = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, 100f * dp)
        val animator: ObjectAnimator
        if (view.alpha == 1f)
            animator = ObjectAnimator.ofPropertyValuesHolder(view, pvha, pvht)
        else
            animator = ObjectAnimator.ofPropertyValuesHolder(view, pvht)
        animator.duration = 200
        animator.start()
    }

    /**
     * フェードインを行う
     *
     * @param view
     */
    fun fadeIn(view: View) {
        view.visibility = View.VISIBLE
        view.translationY = 0f
        val a = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        a.duration = 500
        a.start()
    }
}
