package jp.bellware.echo.filter

/**
 * 一定の窓内における基本統計
 * @param window 窓サイズ
 */
class BasisStats(window: Int) {

    /**
     * 音声キュー
     */
    private val queue: StaticQueue<Float> = StaticQueue(window, 0f)

    /**
     * 平均
     */
    var average = 0f
        private set

    /**
     * 最大
     */
    var max = 0f
        private set

    /**
     * 合計
     */
    var sum = 0f
        private set


    /**
     * 音声パケットを追加する
     */
    fun add(s: Float) {
        this.queue.add(s)
    }

    /**
     * 計算する
     */
    fun calculate() {
        max = java.lang.Float.MIN_VALUE
        sum = 0f
        for (i in 0 until queue.size) {
            val v = queue[i]
            sum += v
            if (v > max)
                max = v
        }
        average = sum / queue.capacity
    }

    /**
     * リセットする
     */
    fun reset() {
        queue.clear()
    }
}
