package jp.bellware.echo.util.filter

import io.kotlintest.matchers.floats.shouldBeGreaterThan
import io.kotlintest.matchers.floats.shouldBeLessThan
import io.kotlintest.shouldBe
import org.junit.Test

class PacketConverterTest {
    @Test
    fun convertFromFloat() {
        PacketConverter.convert(listOf(-1f, 0f, 1f).toFloatArray()).toList() shouldBe listOf<Short>((Short.MIN_VALUE + 1).toShort(), 0, Short.MAX_VALUE)
    }

    @Test
    fun convertFromShort() {
        val converted = PacketConverter.convert(listOf<Short>((Short.MIN_VALUE + 1).toShort(), 0, Short.MAX_VALUE).toShortArray()).toList()
        converted[0] shouldBeLessThan -0.999f
        converted[1] shouldBe 0f
        converted[2] shouldBeGreaterThan 0.999f
    }
}