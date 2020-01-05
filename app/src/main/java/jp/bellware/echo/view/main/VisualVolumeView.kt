package jp.bellware.echo.view.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 視覚的に音量を表示するビュー
 */
class VisualVolumeView : View {

    companion object {

        /**
         * ボリューム0の時の半径
         */
        private const val ZERO_RADIUS_DP = 32

        /**
         * ボリュームMAXの時の半径
         */
        private const val MAX_RADIUS_DP = 64
    }

    /**
     * 表示ボリューム
     */
    private var volume = 0f

    /**
     * ビューの幅
     */
    private var viewWidth = 0

    /**
     * ビューの高さ
     */
    private var viewHeight = 0

    private val paint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        //円を描画する
        val dp = resources.displayMetrics.density
        paint.color = 0xffcccccc.toInt()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        val cx = viewWidth / 2
        val cy = viewHeight / 2
        val r = dp * (volume * (MAX_RADIUS_DP - ZERO_RADIUS_DP) + ZERO_RADIUS_DP)
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), r, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.viewWidth = w
        this.viewHeight = h
    }

    /**
     * ボリュームを設定する
     */
    fun setVolume(volume: Float) {
        this.volume = volume
        invalidate()
    }

}
