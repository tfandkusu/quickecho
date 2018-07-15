package jp.bellware.echo.main.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

import jp.bellware.echo.R;

/**
 * 独自のFloatingActionButton
 */
public class MyFAB extends View {
    public MyFAB(Context context) {
        super(context);
        init(null);
    }

    public MyFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private Bitmap bitmap;

    private int size;

    private float sizeDP = 0;

    private ColorStateList colorTint;

    private float shadowOffset = 0;

    private Paint paint = new Paint();

    private Rect src = new Rect();
    private RectF dst = new RectF();

    private int width = 0;
    private int height = 0;

    private float cx;

    private float cy;

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyFAB,
                0, 0);
        float dp = getResources().getDisplayMetrics().density;
        width = (int) ((56 + 4) * dp);
        height = (int) ((56 + 4) * dp);
        cx = (float)width/2;
        cy = 56*dp / 2;
        int resId = a.getResourceId(R.styleable.MyFAB_myFabIcon, R.drawable.ic_mic_white_24dp);
        size = a.getInteger(R.styleable.MyFAB_myFabSize, 56);
        sizeDP = size * dp;
        int colorTintResId = a.getResourceId(R.styleable.MyFAB_myFabColor, R.color.record_button);
        /* 第２引数ある版だとAndroid Studioでエラー */
        colorTint = getResources().getColorStateList(colorTintResId);
        bitmap = BitmapFactory.decodeResource(getResources(), resId);
        shadowOffset = 2 * dp;
        //ソフトウェアレンダリングにする
        this.setLayerType(LAYER_TYPE_SOFTWARE,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float dp = getResources().getDisplayMetrics().density;
        paint.setAntiAlias(true);
        paint.setColor(0xff000000);
        paint.setAlpha(255/4);
        BlurMaskFilter blur = new BlurMaskFilter(2*dp, BlurMaskFilter.Blur.NORMAL);
        paint.setMaskFilter(blur);
        canvas.drawCircle(cx,cy + shadowOffset, sizeDP / 2, paint);
        //本体を描画
        paint.setMaskFilter(null);
        paint.setColor(colorTint.getColorForState(getDrawableState(), colorTint.getDefaultColor()));
        paint.setAlpha(255);
        canvas.drawCircle(cx,cy, sizeDP / 2, paint);
        //アイコンを描画
        paint.setAlpha(255);
        src.left = 0;
        src.top = 0;
        src.right = bitmap.getWidth();
        src.bottom = bitmap.getHeight();
        dst.left = cx - 12 * dp;
        dst.top = cy - 12 * dp;
        dst.right = cx + 12 * dp;
        dst.bottom = cy + 12 * dp;
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}
