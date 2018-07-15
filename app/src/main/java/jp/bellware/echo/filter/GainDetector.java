package jp.bellware.echo.filter;

/**
 * ゲイン取得
 */
public class GainDetector {
    private float max = 0;

    public void filter(float s) {
        if (s < 0)
            s *= -1;
        if (s > max) {
            max = s;
        }
    }

    public void reset() {
        max = 0;
    }

    public float getMax() {
        return max;
    }

}
