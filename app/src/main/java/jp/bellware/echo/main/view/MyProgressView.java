package jp.bellware.echo.main.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import jp.bellware.echo.R;

public class MyProgressView extends View {
    private Handler handler = new Handler();

    public MyProgressView(Context context) {
        super(context);
        animationTask.run();
    }

    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        animationTask.run();
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        animationTask.run();
    }

    private int width;

    private int height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float dp = getResources().getDisplayMetrics().density;
        int size = (int) (64*dp);
        setMeasuredDimension(size,size);
    }

    private float startAngle = 270;

    private float sweepAngle = 10;

    private Paint paint = new Paint();

    private RectF rect = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.primary));
        float dp = getResources().getDisplayMetrics().density;
        //太さ
        float b = 4*dp;
        paint.setStrokeWidth(b);
        rect.left = b/2;
        rect.top = b/2;
        rect.right = width - b/2;
        rect.bottom = height - b/2;
        canvas.drawArc(rect,startAngle,sweepAngle,false,paint);
    }

    private static final int DURATION = 1000;


    private Runnable animationTask = new Runnable() {
        @Override
        public void run() {
            startAnimation();
            handler.postDelayed(this,DURATION*2);
        }
    };


    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    private void startAnimation() {
        //アニメを開始する
        ObjectAnimator startAnimator1 = ObjectAnimator.ofFloat(this,"startAngle",startAngle,startAngle+180);
        startAnimator1.setInterpolator(new LinearInterpolator());
        startAnimator1.setDuration(DURATION);
        startAnimator1.start();
        ObjectAnimator sweepAnimator1 = ObjectAnimator.ofFloat(this,"sweepAngle",10,350);
        sweepAnimator1.setInterpolator(new LinearInterpolator());
        sweepAnimator1.setDuration(DURATION);
        sweepAnimator1.start();
        //
        ObjectAnimator startAnimator2 = ObjectAnimator.ofFloat(this,"startAngle",startAngle+180,startAngle+180+520);
        startAnimator2.setInterpolator(new LinearInterpolator());
        startAnimator2.setDuration(DURATION);
        startAnimator2.setStartDelay(DURATION);
        startAnimator2.start();
        ObjectAnimator sweepAnimator2 = ObjectAnimator.ofFloat(this,"sweepAngle",350,10);
        sweepAnimator2.setInterpolator(new LinearInterpolator());
        sweepAnimator2.setDuration(DURATION);
        sweepAnimator2.setStartDelay(DURATION);
        sweepAnimator2.start();
    }


}
