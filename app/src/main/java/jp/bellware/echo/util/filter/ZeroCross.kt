package jp.bellware.echo.util.filter

/**
 * ゼロクロスを計算する
 */
class ZeroCross {

    /**
     * 音声サンプルキュー
     */
    private val queue = StaticQueue(WINDOW_SIZE, 0f)

    /**
     * トップインデックス
     */
    private var top = 0

    /**
     * ボトムインデックス
     */
    private var bottom = 0

    /**
     * トップの前サンプル符号
     */
    private var topSign = 0

    /**
     * ボトムの前サンプル符号
     */
    private var bottomSign = 0

    /**
     * 零点クロス回数
     */
    var cross = 0
        private set

    /**
     * 音声入力中
     * @return
     */
    val isSpeaking: Boolean
        get() = cross >= CROSS

    /**
     * リセットする
     */
    fun reset() {
        top = 0
        bottom = 0
        topSign = 0
        bottomSign = 0
        cross = 0
    }

    /**
     * サンプルを追加する
     */
    fun add(s: Float) {
        //トップについて
        //クロス判定
        if (topSign == 1 && s < 0) {
            ++cross
            topSign = 0
        } else if (topSign == -1 && s > 0) {
            ++cross
            topSign = 0
        }
        //符号
        if (s < -AMP)
            topSign = -1
        else if (s > AMP)
            topSign = 1
        //ボトムについて
        bottom = top - WINDOW_SIZE
        if (bottom >= 0) {
            val t = queue[queue.size - 1]
            //クロス判定
            if (bottomSign == 1 && t < 0) {
                --cross
                bottomSign = 0
            } else if (bottomSign == -1 && t > 0) {
                --cross
                bottomSign = 0
            }
            //符号
            if (t < -AMP)
                bottomSign = -1
            else if (t > AMP)
                bottomSign = 1
        }
        //キューに追加
        queue.add(s)
        top++
    }

    companion object {

        /**
         * トップインデックスとボトムインデックスの差
         */
        private val WINDOW_SIZE = 4410

        /**
         * 振幅基準値
         */
        private val AMP = 0.01f

        /**
         * クロス基準値
         */
        private val CROSS = 10
    }

}
