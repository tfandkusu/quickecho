package jp.bellware.echo.filter

/**
 * フェードアウト
 * @param length 全体の長さ
 * @param duration フェードアウト期間
 */
class FadeOut(
        private val length: Int,
        private val duration: Int) {

    /**
     * 現在処理位置
     */
    private var index: Int = 0

    /**
     * フィルターをかける
     */
    fun filter(s: Float): Float {
        val v: Float
        if (index > length - duration) {
            v = s * (length - index) / duration
        } else {
            v = s
        }
        index += 1
        return v
    }
}
