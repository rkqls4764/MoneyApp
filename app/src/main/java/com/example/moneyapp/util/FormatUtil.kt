package com.example.moneyapp.util

import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

/* YearMonth -> String (yyyy-MM) */
fun YearMonth.toYmString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.KOREAN)
    return this.format(formatter)
}

/* LocalDateTime -> String (yyyy-MM-dd) */
fun LocalDateTime.toYmdString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREAN)
    return this.format(formatter)
}

/* LocalDateTime -> String (yyyy-MM-dd (E)) */
fun LocalDateTime.toYmdeString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)", Locale.KOREAN)
    return this.format(formatter)
}

/* LocalDateTime -> String (yyyy-MM-dd HH:mm) */
fun LocalDateTime.toYmdHmString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm", Locale.KOREAN)
    return this.format(formatter)
}

/* LocalDateTime -> String (HH:mm) */
fun LocalDateTime.toHmString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

/* LocalDateTime -> String (yyyy년) */
fun LocalDateTime.toYDisplayString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy년", Locale.KOREAN)
    return this.format(formatter)
}

/* LocalDateTime -> String (yyyy년 mm월) */
fun LocalDateTime.toYmDisplayString(): String {
    val weekOfMonth = this.get(
        WeekFields.of(Locale.KOREA).weekOfMonth()
    )
    return "${this.year}년 ${this.monthValue}월"
}

/* LocalDateTime -> String (yyyy년 mm월 week주) */
fun LocalDateTime.toYmWeekDisplayString(): String {
    val day = this.dayOfMonth
    val block = ((day - 1) / 7) + 1
    return "${this.year}년 ${this.monthValue}월 ${block}주"
}

/* 숫자 천 단위마다 콤마 구분 */
fun formatMoney(num: Long): String {
    return String.format("%,d", num)
}