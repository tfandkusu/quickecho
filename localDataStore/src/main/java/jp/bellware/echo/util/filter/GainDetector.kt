package jp.bellware.echo.util.filter

/**
 * ゲイン取得
 */
class GainDetector {
    /**
     * ゲイン
     */
    var max = 0f
        private set

    /**
     * サンプルを追加する
     */
    fun add(sample: Float) {
        var s = sample
        if (s < 0)
            s *= -1f
        if (s > max) {
            max = s
        }
    }

    /**
     * リセットする
     */
    fun reset() {
        max = 0f
    }
}
