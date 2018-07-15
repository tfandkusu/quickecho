package jp.bellware.echo.filter;

/**
 * 視覚的に分かりやすい音量表示を行う。
 */
public interface VisualVolumeProcessor {
    void reset();

    void filter(float s);

    float getVolume();

    /**
     * 音声が録音されているフラグ
     * @return
     */
    boolean isIncludeSound();
}
