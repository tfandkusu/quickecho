package jp.bellware.echo.util.filter

import io.kotlintest.shouldBe
import org.junit.Test

class BasisStatsTest {
    @Test
    fun test() {
        val stats = BasisStats(3)
        stats.average shouldBe 0f
        stats.max shouldBe 0f
        stats.sum shouldBe 0f
        stats.add(-1f)
        stats.add(0.25f)
        stats.add(-0.5f)
        stats.add(0.75f)
        stats.calculate()
        stats.average shouldBe 0.5f
        stats.max shouldBe 0.75f
        stats.sum shouldBe 1.5f
        stats.reset()
        stats.calculate()
        stats.average shouldBe 0f
        stats.max shouldBe 0f
        stats.sum shouldBe 0f
    }
}
