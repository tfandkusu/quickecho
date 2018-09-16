package jp.bellware.echo.main.view

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * 爆発ビュー
 */
class ExplosionView : View {


    /**
     * ビューの幅
     */
    private var viewWidth = 0f

    /**
     * ビューの高さ
     */
    private var viewHeight = 0f

    /**
     * 中心X
     */
    private var centerX = 0f

    /**
     * 中心Y
     */
    private var centerY = 0f

    private val paint = Paint()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * アニメーション進行度
     */
    private var radius = 0f

    /**
     * アニメーション進行度を設定する
     */
    fun setRadius(radius : Float){
        this.radius = radius
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        //円を描画する
        paint.color = -0x333334 //0xffcccccc
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.viewWidth = w.toFloat()
        this.viewHeight = h.toFloat()
    }


    /**
     * 録音開始アニメーションを開始する
     */
    fun startRecordAnimation() {
        val dp = context.resources.displayMetrics.density
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横画面
            centerX = (32 + 64) * dp
            centerY = viewHeight / 2
        } else {
            //縦画面
            centerX = viewWidth / 2
            centerY = (viewHeight - 104 * dp) / 2
        }
        //最大半径
        val radius = (Math.sqrt((centerY * centerY + centerX * centerX).toDouble()) * 1.2f).toFloat()
        //大きくしつつ透明にする
        val pvhR = PropertyValuesHolder.ofFloat("radius", 0f, radius)
        val pvhA = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(this, pvhA, pvhR)
        animator.duration = 500
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

}
