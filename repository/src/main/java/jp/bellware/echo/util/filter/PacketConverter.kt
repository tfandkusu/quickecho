package jp.bellware.echo.util.filter

/**
 * 音声パケットの型を変換する
 */
object PacketConverter {
    /**
     * shortの最大値に1を足したもの
     */
    private const val SHORT_MAX_P1 = 32768

    /**
     * flaot型に変換する
     * @param data
     * @return
     */
    fun convert(data: ShortArray): FloatArray {
        val fd = FloatArray(data.size)
        for (i in data.indices) {
            fd[i] = data[i].toFloat() / SHORT_MAX_P1
        }
        return fd
    }

    /**
     * short型に変換する
     * @param data
     * @return
     */
    fun convert(data: FloatArray): ShortArray {
        val sd = ShortArray(data.size)
        for (i in data.indices) {
            sd[i] = (data[i] * java.lang.Short.MAX_VALUE).toShort()
        }
        return sd
    }
}