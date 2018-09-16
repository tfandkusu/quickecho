package jp.bellware.echo

import jp.bellware.echo.filter.ZeroCross
import junit.framework.TestCase

/**
 * ゼロクロスのテスト。ユーザが話しているかいないかの判定はゼロクロスによって行われる。
 */
class ZeroCrossTester : TestCase() {
    fun testZero1() {
        val zc = ZeroCross()
        for (i in 0..9999) {
            zc.add(0f)
        }
        assertEquals(0, zc.cross)
    }

    fun testZero2() {
        val zc = ZeroCross()
        for (i in 0..9999) {
            if (i % 2 == 0)
                zc.add(0.005f)
            else
                zc.add(-0.005f)
        }
        assertEquals(0, zc.cross)
    }

    fun testSome() {
        val zc = ZeroCross()
        for (i in 0..4999) {
            if (i == 1)
                zc.add(0.2f)
            else if (i == 2)
                zc.add(-0.2f)
            else if (i == 1000)
                zc.add(0.2f)//1回目
            else if (i == 1001)
                zc.add(-0.2f)//2回目
            else if (i == 2000)
                zc.add(0.005f)//基準値以下
            else if (i == 2001)
                zc.add(-0.005f)//基準値以下
            else if (i == 3000)
                zc.add(0.2f)//3回目
            else if (i == 3001)
                zc.add(-0.2f)//4回目
            else
                zc.add(0f)
        }
        assertEquals(4, zc.cross)
    }

}
