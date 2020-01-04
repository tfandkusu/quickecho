package jp.bellware.echo.util.filter

import io.kotlintest.shouldBe
import org.junit.Test

class GainDetectorTest {
    @Test
    fun test() {
        val gd = GainDetector()
        gd.max shouldBe 0f
        gd.add(0.25f)
        gd.max shouldBe 0.25f
        gd.add(0.5f)
        gd.max shouldBe 0.5f
        gd.add(-0.75f)
        gd.max shouldBe 0.75f
        gd.reset()
        gd.max shouldBe 0f
    }
}