package com.example.moneyapp.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/* LocalDateTime -> String (yyyy-MM-dd (E)) */
fun LocalDateTime.toYmdeString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)", Locale.KOREAN)
    return this.format(formatter)
}

/* LocalDateTime -> String (HH:mm) */
fun LocalDateTime.toHmString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

/* 숫자 천 단위마다 콤마 구분 */
fun formatMoney(num: Long): String {
    return String.format("%,d", num)
}