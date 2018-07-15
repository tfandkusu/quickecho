package jp.bellware.echo.main.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import jp.bellware.echo.R;

/**
 * 爆発ビュー
 */
public class ExplosionView extends View {

    public ExplosionView(Context context) {
        super(context);
        init();
    }

    public ExplosionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExplosionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
    }

    @BindDimen(R.dimen.dp)
    float dp;

    private float width;

    private float height;

    private float centerX;

    private float centerY;


    /**
     * アニメーション進行度
     */
    private float radius = 0;

    private Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(0xffcccccc);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, getRadius(), paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }


    /**
     * 録音開始アニメーションを開始する
     */
    public void startRecordAnimation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横画面
            centerX = (32 + 64) * dp;
            centerY = height / 2;
        } else {
            //縦画面
            centerX = width / 2;
            centerY = (height - 104 * dp) / 2;
        }
        //最大半径
        float radius = (float) (Math.sqrt(centerY * centerY + centerX * centerX) * 1.2f);
        PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat("radius", 0, radius);
        PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha", 1, 0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, pvhA, pvhR);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

}
