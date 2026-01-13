package com.example.moneyapp.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

/* Date -> String (yyyy-MM-dd (E)) */
fun Date.toYmdeString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)")
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}

/* Date -> String (HH:mm) */
fun Date.toHmString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(formatter)
}

/* 숫자 천 단위마다 콤마 구분 */
fun formatMoney(num: Long): String {
    return String.format("%,d", num)
}