package com.example.moneyapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(
    tableName = "transaction_table",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,             // 연결 대상
            parentColumns = ["id"],               // 연결할 키
            childColumns = ["categoryId"],        // 내 컬럼명
            onDelete = ForeignKey.SET_NULL        // 카테고리 삭제될 시, null로 변경
        )
    ]
)
data class MoneyTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // 내역 id

    val amount: Long, // 금액
    val description: String, // 내역 이름
    val memo: String?, // 메모
    val type: TransactionType, // 수입/지출
    val categoryId: Long? = null, // 카테고리 id(nullable)
    val date: LocalDateTime, // 날짜+시간

    val createdAt: Long = System.currentTimeMillis(), // 생성 시간
    val updatedAt: Long = System.currentTimeMillis() // 수정 시간
)