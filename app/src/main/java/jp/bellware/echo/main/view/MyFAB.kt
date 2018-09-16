package jp.bellware.echo.main.view

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import jp.bellware.echo.R

/**
 * 独自のFloatingActionButton
 */
class MyFAB : View {

    /**
     * アイコンビットマップ
     */
    private lateinit var bitmap: Bitmap

    /**
     * FABのサイズ。DP単位で56か40
     */
    private var size: Int = 0

    /**
     * ピクセル単位のサイズ
     */
    private var sizeDP = 0f

    /**
     * FABの色
     */
    private lateinit var colorTint: ColorStateList

    /**
     * 影オフセット
     */
    private var shadowOffset = 0f

    private val paint = Paint()

    private val src = Rect()
    private val dst = RectF()

    /**
     * ビューの幅
     */
    private var viewWidth = 0
    /**
     * ビューの高さ
     */
    private var viewHeight = 0

    /**
     * 中心X
     */
    private var cx: Float = 0f

    /**
     * 中心Y
     */
    private var cy: Float = 0f

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
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MyFAB,
                0, 0)
        val dp = resources.displayMetrics.density
        viewWidth = ((56 + 4) * dp).toInt()
        viewHeight = ((56 + 4) * dp).toInt()
        cx = viewWidth.toFloat() / 2
        cy = 56 * dp / 2
        val resId = a.getResourceId(R.styleable.MyFAB_myFabIcon, R.drawable.ic_mic_white_24dp)
        size = a.getInteger(R.styleable.MyFAB_myFabSize, 56)
        sizeDP = size * dp
        val colorTintResId = a.getResourceId(R.styleable.MyFAB_myFabColor, R.color.record_button)
        /* 第２引数ある版だとAndroid Studioでエラー */
        colorTint = resources.getColorStateList(colorTintResId)
        bitmap = BitmapFactory.decodeResource(resources, resId)
        shadowOffset = 2 * dp
        //ソフトウェアレンダリングにする
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val dp = resources.displayMetrics.density
        paint.isAntiAlias = true
        paint.color = -0x1000000
        paint.alpha = 255 / 4
        val blur = BlurMaskFilter(2 * dp, BlurMaskFilter.Blur.NORMAL)
        paint.maskFilter = blur
        canvas.drawCircle(cx, cy + shadowOffset, sizeDP / 2, paint)
        //本体を描画
        paint.maskFilter = null
        paint.color = colorTint.getColorForState(drawableState, colorTint!!.defaultColor)
        paint.alpha = 255
        canvas.drawCircle(cx, cy, sizeDP / 2, paint)
        //アイコンを描画
        paint.alpha = 255
        src.left = 0
        src.top = 0
        src.right = bitmap.width
        src.bottom = bitmap.height
        dst.left = cx - 12 * dp
        dst.top = cy - 12 * dp
        dst.right = cx + 12 * dp
        dst.bottom = cy + 12 * dp
        canvas.drawBitmap(bitmap, src, dst, paint)

    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        invalidate()
    }
}
