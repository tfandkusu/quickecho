package jp.bellware.echo.repository.data

import io.kotlintest.shouldBe
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class YMDTest {

    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    @Before
    fun setUp() {
        sdf.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @Test
    fun toYMD() {
        val date1 = sdf.parse("2020/10/22 00:00:00")
        toYMD(date1.time) shouldBe YMD(2020, 10, 22)
        val date2 = sdf.parse("2020/10/22 23:59:59")
        toYMD(date2.time) shouldBe YMD(2020, 10, 22)
        val date3 = sdf.parse("2020/10/23 00:00:00")
        toYMD(date3.time) shouldBe YMD(2020, 10, 23)
    }
}
