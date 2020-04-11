package jp.bellware.echo.util.filter

import io.kotlintest.shouldBe
import org.junit.Test

class FirstCutTest {
    @Test
    fun test() {
        val fc = FirstCut(2)
        fc.filter(1.0f) shouldBe 0.0f
        fc.filter(1.0f) shouldBe 0.0f
        fc.filter(1.0f) shouldBe 1.0f
        fc.filter(1.0f) shouldBe 1.0f
    }
}