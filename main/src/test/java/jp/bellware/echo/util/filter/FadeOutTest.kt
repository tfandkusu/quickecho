package jp.bellware.echo.util.filter

import io.kotlintest.shouldBe
import org.junit.Test

class FadeOutTest {
    @Test
    fun test() {
        val fo = FadeOut(5, 4)
        fo.filter(1f) shouldBe 1f
        fo.filter(1f) shouldBe 1f
        fo.filter(1f) shouldBe 0.75f
        fo.filter(1f) shouldBe 0.5f
        fo.filter(1f) shouldBe 0.25f
    }
}