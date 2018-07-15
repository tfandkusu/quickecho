package jp.bellware.echo.main.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import jp.bellware.echo.R;

/**
 * 色の濃さで録音ボリュームを教えるビュー
 */
public class ColorVolumeView extends View {
    public ColorVolumeView(Context context) {
        super(context);
    }

    public ColorVolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorVolumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int width;

    private int height;

    /**
     * 値
     */
    private float volume = 0.0f;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int color = getResources().getColor(R.color.primary);
        int alpha = (int) (255*volume/4);
        canvas.drawColor( (0xffffff & color) | (alpha << 24));
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        invalidate();
    }
}
