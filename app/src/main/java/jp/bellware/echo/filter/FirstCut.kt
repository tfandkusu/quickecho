package jp.bellware.echo.filter

/**
 * 最初をカットする
 * @param first カット期間
 */
class FirstCut(private val first: Int) {
    private var index = 0

    /**
     * フィルターをかける
     */
    fun filter(s: Float): Float {
        var d = 0f
        if (index >= first)
            d = s
        ++index
        return d
    }

    /**
     * リセット
     */
    fun reset() {
        index = 0
    }
}
