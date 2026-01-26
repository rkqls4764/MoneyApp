package com.gabeen.moneyapp.data.entity

// 통계 데이터를 담을 클래스
data class CategoryStat(
    val categoryId: Long,       // 카테고리 아이디
    val categoryName: String,   // 카테고리 이름
    val type: TransactionType,  // 수입/지출
    val totalAmount: Long       // 카테고리 총 합계(기간 내)
)