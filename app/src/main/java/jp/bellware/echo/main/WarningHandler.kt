package jp.bellware.echo.main

import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import jp.bellware.echo.R

/**
 * 警告表示担当
 */
class WarningHandler {
    private var activity: Activity? = null

    private val handler = Handler()

    /**
     * 警告表示全体
     */
    private lateinit var view: View

    /**
     * 警告表示のテキスト
     */
    private lateinit var textView: TextView

    private val task = Runnable {
        val a = AlphaAnimation(1f, 0f)
        a.duration = 300
        a.fillAfter = true
        view.startAnimation(a)
    }

    fun onCreate(activity: Activity) {
        this.activity = activity
        view = activity.findViewById(R.id.warning_mute)
        view.visibility = View.GONE
        textView = activity.findViewById<View>(R.id.warning_mute_text) as TextView
    }

    fun show(resId: Int) {
        textView.setText(resId)
        val a = AlphaAnimation(0f, 1f)
        a.duration = 300
        view.startAnimation(a)
        view.visibility = View.VISIBLE
        handler.removeCallbacks(task)
        handler.postDelayed(task, 3000)
    }

    fun onResume() {

    }

    fun onPause() {
        handler.removeCallbacks(task)
        view.visibility = View.GONE
    }
}
