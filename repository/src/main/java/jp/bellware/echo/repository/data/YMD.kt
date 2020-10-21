package jp.bellware.echo.repository.data

import java.util.*

/**
 * 日付表現
 * @param year 年
 * @param month 月
 * @param day 日
 */
data class YMD(val year: Int, val month: Int, val day: Int)

fun toYMD(timeMillis: Long): YMD {
    val calendar = GregorianCalendar.getInstance()
    calendar.timeInMillis = timeMillis
    return YMD(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
    )
}

