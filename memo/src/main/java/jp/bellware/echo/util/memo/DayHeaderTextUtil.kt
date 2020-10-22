package jp.bellware.echo.util.memo

import jp.bellware.echo.repository.data.YMD
import java.text.SimpleDateFormat
import java.util.*

object DayHeaderTextUtil {
    private val thisYear = SimpleDateFormat("MM/dd(EEE)", Locale.US)

    private val withYear = SimpleDateFormat("yyyy/MM/dd(EEE)", Locale.US)

    /**
     * 日本語の曜日一覧
     */
    private val DAYS = listOf("", "日", "月", "火", "水", "木", "金", "土")


    fun thisYear(ymd: YMD, locale: Locale): String {
        val calendar = toCalendar(ymd)
        return if (locale == Locale.JAPAN || locale == Locale.JAPANESE) {
            String.format("%d月%d日（%s）", ymd.month, ymd.day, DAYS[calendar.get(Calendar.DAY_OF_WEEK)])
        } else {
            thisYear.format(calendar)
        }
    }

    fun withYear(ymd: YMD, locale: Locale): String {
        val calendar = toCalendar(ymd)
        return if (locale == Locale.JAPAN || locale == Locale.JAPANESE) {
            String.format("%d年%d月%d日（%s）", ymd.year, ymd.month, ymd.day, DAYS[calendar.get(Calendar.DAY_OF_WEEK)])
        } else {
            withYear.format(calendar)
        }
    }

    private fun toCalendar(ymd: YMD): Calendar {
        val calendar = GregorianCalendar.getInstance(TimeZone.getDefault())
        calendar.set(Calendar.YEAR, ymd.year)
        calendar.set(Calendar.MONTH, ymd.month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, ymd.day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }
}
