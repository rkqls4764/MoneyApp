package com.gabeen.moneyapp.data.database

import androidx.room.TypeConverter
import com.gabeen.moneyapp.data.entity.TransactionType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// 직접 호출x Room이 알아서 사용
class Converters {

    // LocalDateTime <-> Long 변환
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        // {myValue -> Date(myValue)}와 같음. it은 넘어오는 데이터가 하나일 때 사용
        // value?.let : value가 null이면 null을 넘기고, 아니라면 let 함수 실행
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        // date?.time : date null이면 null을 넘기고, 아니라면 time 속성 넘기기
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    // Enum <-> String 변환
    @TypeConverter
    fun fromTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun transactionTypeToString(type: TransactionType): String {
        return type.name
    }
}