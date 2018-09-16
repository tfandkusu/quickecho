package jp.bellware.echo.main.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import jp.bellware.echo.R

/**
 * カードビュー
 */
class MyCardView : View {

    /**
     * ビューの幅
     */
    private var viewWidth = 0

    /**
     * ビューの高さ
     */
    private var viewHeight = 0

    /**
     * 色
     */
    private var color = 0

    private val paint = Paint()

    private val rect = RectF()

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.MyCardView,
                    0, 0)
            color = a.getColor(R.styleable.MyCardView_myCardColor, R.color.white)
        } else {
            color = resources.getColor(R.color.white)
        }
        //
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.viewWidth = w
        this.viewHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        val dp = resources.displayMetrics.density
        val margin = 8 * dp
        val offset = 2 * dp
        val round = 2 * dp
        //影
        val blur = BlurMaskFilter(4 * dp, BlurMaskFilter.Blur.NORMAL)
        paint.maskFilter = blur
        paint.color = -0x1000000
        paint.alpha = 255 / 4
        rect.left = margin
        rect.top = margin + offset
        rect.right = viewWidth - margin
        rect.bottom = viewHeight - margin + offset
        canvas.drawRoundRect(rect, round, round, paint)
        //本体
        paint.maskFilter = null
        paint.color = color
        paint.alpha = 255
        rect.left = margin
        rect.top = margin
        rect.right = viewWidth - margin
        rect.bottom = viewHeight - margin
        canvas.drawRoundRect(rect, round, round, paint)


    }
}
