package jp.bellware.echo.filter


/**
 * ゼロ点クロスを用いた視覚的ボリューム
 */
class ZeroCrossRecordVisualVolumeProcessor : VisualVolumeProcessor {
    /**
     * ゼロクロス担当
     */
    private val zc = ZeroCross()

    /**
     * ボリュームが増える最大速度
     */
    private val upSpeed = 0.03f

    /**
     * ボリュームが減る最大速度
     */
    private val downSpeed = 0.03f

    /**
     * ボリューム基準
     */
    private var base = 0f

    /**
     * 現在ボリューム
     */
    private var volume = 0f

    /**
     * サンプルインデックス
     */
    private var index = 0

    /**
     * 音声を含んでいるフラグ
     */
    private var include = false

    /**
     * ボリューム計測用
     */
    private val packet = BasisStats(PACKET_SIZE)


    override fun reset() {
        zc.reset()
        volume = 0f
        index = 0
        base = 0f
        include = false
        packet.reset()
    }

    override fun filter(s: Float) {
        var s = s
        zc.add(s)
        //絶対値にする
        s = Math.abs(s)
        packet.add(s)
        if (index % PACKET_SIZE == PACKET_SIZE - 1) {
            /**
             * 現在ボリューム
             */
            val cv: Float
            if (zc.isSpeaking) {
                include = true
                packet.calculate()
                //入力中
                if (base == 0f) {
                    base = packet.max
                    if (base == 0f)
                        base = 0.01f
                }
                cv = packet.max / base
            } else {
                //無音
                cv = 0f
            }
            if (cv < volume - downSpeed) {
                volume -= downSpeed
            } else if (cv > volume + upSpeed) {
                volume += upSpeed
            } else {
                volume = cv
            }
            if (volume < 0)
                volume = 0f
            else if (volume > 1)
                volume = 1f
        }
        ++index
    }

    override fun getVolume(): Float {
        //使わなくなった。
        return volume
    }

    override fun isIncludeSound(): Boolean {
        //ここだけ使う
        return include
    }

    companion object {

        /**
         * 現在ボリューム取得のためのサイズ
         */
        private const val PACKET_SIZE = 441

    }
}
