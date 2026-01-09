package com.example.moneyapp.data.database

import androidx.room.TypeConverter
import com.example.moneyapp.data.entity.TransactionType
import java.util.Date

// 직접 호출x Room이 알아서 사용
class Converters {

    // Date <-> Long 변환
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        // {myValue -> Date(myValue)}와 같음. it은 넘어오는 데이터가 하나일 때 사용
        // value?.let : value가 null이면 null을 넘기고, 아니라면 let 함수 실행
        return value?.let {Date(it)}
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        // date?.time : date null이면 null을 넘기고, 아니라면 time 속성 넘기기
        return date?.time
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