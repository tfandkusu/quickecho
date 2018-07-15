package jp.bellware.echo.main.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 視覚的に音量を表示するビュー
 */
public class VisualVolumeView extends View {

    public VisualVolumeView(Context context) {
        super(context);
    }

    public VisualVolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VisualVolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 表示ボリューム
     */
    private float volume = 0;

    private int width;

    private int height;

    private int ZERO_RADIUS_DP = 32;

    private int MAX_RADIUS_DP = 64;

    private Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        float dp = getResources().getDisplayMetrics().density;
        paint.setColor(0xffcccccc);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        int cx = width/2;
        int cy = height/2;
        float r = dp*(volume*(MAX_RADIUS_DP - ZERO_RADIUS_DP) + ZERO_RADIUS_DP);
        canvas.drawCircle(cx,cy,r,paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        invalidate();
    }
}
