package com.example.moneyapp.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

/* Date -> String (yyyy-MM-dd) */
fun Date.toYmdString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
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