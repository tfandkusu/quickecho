package jp.bellware.echo.filter;

/**
 * 視覚的に分かりやすい音量表示を行う。録音用。
 */
public class PlayVisualVolumeProcessor implements VisualVolumeProcessor {

    private float vv = 0;

    public void reset() {
        vv = 0;
    }

    public void filter(float s) {
        if (s < 0)
            s *= -1;
        if (s > vv)
            vv = s;
        else {
            //少しづつ後退させる
            vv -= 2f * 1f / 44100;
        }
    }

    public float getVolume() {
        return vv;
    }

    @Override
    public boolean isIncludeSound() {
        return false;
    }
}
