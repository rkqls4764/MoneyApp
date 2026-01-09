package com.example.moneyapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // 카테고리 아이디

    val name: String, // 카테고리 이름
    val type: TransactionType, // 수입, 지출 구분

    val createdAt: Long = System.currentTimeMillis(), // 생성 날짜
    val updatedAt: Long = System.currentTimeMillis() // 수정 날짜
) {
}