package jp.bellware.echo.data

/**
 * 一定サイズを超えないキュー。
 * @param capacity キャパシティ
 * @param defVal デフォルト値
 */
class StaticQueue<T>(
        val capacity: Int,val defVal : T) {
    private var offset: Int = 0
    private var array = mutableListOf<T>()

    private var size: Int = 0

    init {
        init()
    }

    private fun init() {
        array = mutableListOf()
        for (i in 0 until capacity) {
            array.add(defVal)
        }
        offset = 0
        size = 0
    }

    fun clear() {
        init()
    }


    fun add(v: T) {
        array[offset] = v
        ++offset
        if (size < capacity)
            ++size
        if (capacity <= offset)
            offset = 0
    }

    /**
     * 値を取得する。インデックスが大きい方が古い。
     *
     * @param i
     * @return
     */
    operator fun get(i: Int): T {
        var index = offset - i - 1
        if (index < 0)
            index += array.size
        return array[index]
    }

    fun size(): Int {
        return size
    }

}
