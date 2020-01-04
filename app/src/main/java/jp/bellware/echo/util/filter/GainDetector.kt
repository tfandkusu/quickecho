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
     * パケットを追加する
     */
    fun add(s: Float) {
        var s = s
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
