package jp.bellware.echo.main.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import jp.bellware.echo.R;

/**
 *
 */
public class MyCardView extends View {
    public MyCardView(Context context) {
        super(context);
        init(null);
    }

    public MyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private int width;

    private int height;

    private int color;

    private Paint paint = new Paint();

    private RectF rect = new RectF();

    private void init(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.MyCardView,
                    0, 0);
            color = a.getColor(R.styleable.MyCardView_myCardColor, R.color.white);
        }else{
            color = getResources().getColor(R.color.white);
        }
        //
        this.setLayerType(LAYER_TYPE_SOFTWARE,null);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float dp = getResources().getDisplayMetrics().density;
        float margin = 8*dp;
        float offset = 2*dp;
        float round = 2*dp;
        //影
        BlurMaskFilter blur = new BlurMaskFilter(4*dp, BlurMaskFilter.Blur.NORMAL);
        paint.setMaskFilter(blur);
        paint.setColor(0xff000000);
        paint.setAlpha(255/4);
        rect.left = margin;
        rect.top = margin + offset;
        rect.right = width - margin;
        rect.bottom = height - margin + offset;
        canvas.drawRoundRect(rect, round,round,paint);
        //本体
        paint.setMaskFilter(null);
        paint.setColor(color);
        paint.setAlpha(255);
        rect.left = margin;
        rect.top = margin;
        rect.right = width - margin;
        rect.bottom = height - margin;
        canvas.drawRoundRect(rect, round,round,paint);


    }
}
