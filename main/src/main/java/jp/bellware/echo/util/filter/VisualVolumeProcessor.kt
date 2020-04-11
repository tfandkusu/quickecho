package jp.bellware.echo.util.filter

/**
 * 視覚的に分かりやすい音量表示を行う。
 */
interface VisualVolumeProcessor {

    /**
     * ボリュームを取得する
     * @return
     */
    fun getVolume(): Float

    /**
     * 音声が録音されているフラグ
     * @return
     */
    fun isIncludeSound(): Boolean

    /**
     * リセットする
     */
    fun reset()

    /**
     * サンプルを追加する
     * @param s サンプル
     */
    fun add(s: Float)
}
