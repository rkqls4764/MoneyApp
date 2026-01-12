package com.example.moneyapp.data.entity

import androidx.room.Embedded
import androidx.room.Relation

// entity X, db table X
// 카테고리와 내역을 함께 묶어서 조회하는 응답 클래스
data class TransactionWithCategory(
    @Embedded val transaction: MoneyTransaction,
    @Relation(
        parentColumn = "categoryId",   // @Embedded 가 들고 있는 외래키
        entityColumn = "id"            // 함께 묶을 클래스가 들고 있는 진짜 키
    )
    val category: Category?
)