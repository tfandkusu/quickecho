package jp.bellware.echo.main.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * 影を表示する
 */
public class ToolbarShadowView extends View {
    public ToolbarShadowView(Context context) {
        super(context);
        init();
    }

    public ToolbarShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToolbarShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        this.setLayerType(LAYER_TYPE_SOFTWARE,null);
    }

    private int width;

    private int height;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float dp = getResources().getDisplayMetrics().density;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(widthSize, (int) (64*dp));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    private Paint paint = new Paint();

    private RectF rect = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        float dp = getResources().getDisplayMetrics().density;
        //影
        BlurMaskFilter blur = new BlurMaskFilter(4*dp, BlurMaskFilter.Blur.NORMAL);
        paint.setMaskFilter(blur);
        paint.setColor(0xff000000);
        paint.setAlpha(255/4);
        rect.left = 0;
        rect.top = 0;
        rect.right = width;
        rect.bottom = height - 6*dp;
        canvas.drawRect(rect,paint);
    }
}
