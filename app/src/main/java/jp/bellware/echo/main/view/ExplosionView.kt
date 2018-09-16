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


    private var width: Float = 0.toFloat()

    private var height: Float = 0.toFloat()

    private var centerX: Float = 0.toFloat()

    private var centerY: Float = 0.toFloat()




    private val paint = Paint()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * アニメーション進行度
     */
    private var radius = 0f

    fun setRadius(radius : Float){
        this.radius = radius
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        paint.color = -0x333334
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.width = w.toFloat()
        this.height = h.toFloat()
    }


    /**
     * 録音開始アニメーションを開始する
     */
    fun startRecordAnimation() {
        val dp = context.resources.displayMetrics.density
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横画面
            centerX = (32 + 64) * dp
            centerY = height / 2
        } else {
            //縦画面
            centerX = width / 2
            centerY = (height - 104 * dp) / 2
        }
        //最大半径
        val radius = (Math.sqrt((centerY * centerY + centerX * centerX).toDouble()) * 1.2f).toFloat()
        val pvhR = PropertyValuesHolder.ofFloat("radius", 0f, radius)
        val pvhA = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(this, pvhA, pvhR)
        animator.duration = 500
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

}
