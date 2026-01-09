package com.example.moneyapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transaction_table")
class MoneyTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // 내역 id

    val amount: Long, // 금액
    val description: String, // 내역 이름
    val memo: String, // 메모
    val type: TransactionType, // 수입/지출
    val categoryId: Long?, // 카테고리 id(nullable)
    val date: Date, // 날짜

    val createdAt: Long = System.currentTimeMillis(), // 생성 시간
    val updatedAt: Long = System.currentTimeMillis() // 수정 시간
)