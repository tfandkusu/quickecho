package jp.bellware.echo

import junit.framework.TestCase

import jp.bellware.echo.data.StaticQueue

/**
 * 一定のサイズを超えないキューのテスト
 */
class StaticQueueTester : TestCase() {
    fun test() {
        val queue = StaticQueue<Int>(3,0)
        assertEquals(0, queue.size)
        //
        queue.add(1)
        assertEquals(1, queue.size)
        assertEquals(1, queue.get(0).toInt())
        //
        queue.add(2)
        assertEquals(2, queue.size)
        assertEquals(2, queue.get(0).toInt())
        assertEquals(1, queue.get(1).toInt())
        //
        queue.add(3)
        assertEquals(3, queue.size)
        assertEquals(3, queue.get(0).toInt())
        assertEquals(2, queue.get(1).toInt())
        assertEquals(1, queue.get(2).toInt())
        // 4番目の値を入れると、1番目がなくなる
        queue.add(4)
        assertEquals(3, queue.size)
        assertEquals(4, queue.get(0).toInt())
        assertEquals(3, queue.get(1).toInt())
        assertEquals(2, queue.get(2).toInt())
    }
}
