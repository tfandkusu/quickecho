package jp.bellware.echo.view.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import jp.bellware.echo.flux.R

/**
 * 視覚的に音量を表示するビュー
 */
class VisualVolumeView : View {


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

    companion object {
        private const val DEFAULT_MIN_RADIUS_DP = 32f

        private const val DEFAULT_MAX_RADIUS_DP = 64f
    }

    private val dp = resources.displayMetrics.density

    private var minRadius = 32 * dp

    private var maxRadius = 64 * dp

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.VisualVolumeView,
                0, 0).apply {
            try {
                maxRadius = getDimension(R.styleable.VisualVolumeView_maxRadiusDp, DEFAULT_MAX_RADIUS_DP * dp)
                minRadius = getDimension(R.styleable.VisualVolumeView_minRadiusDp, DEFAULT_MIN_RADIUS_DP * dp)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension((2 * maxRadius).toInt(), (2 * maxRadius).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        //円を描画する
        paint.color = 0xffcccccc.toInt()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        val cx = viewWidth / 2
        val cy = viewHeight / 2
        val r = volume * (maxRadius - minRadius) + minRadius
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
