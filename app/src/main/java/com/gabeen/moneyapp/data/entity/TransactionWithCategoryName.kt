package com.gabeen.moneyapp.data.entity

import java.time.LocalDateTime

// csv 파일 export 용
// entity X, db table X
// 카테고리 이름과 내역을 함께 묶어서 조회하는 응답 클래스
data class TransactionWithCategoryName(

    val amount: Long, // 금액
    val description: String, // 내역 이름
    val memo: String?, // 메모
    val type: TransactionType, // 수입/지출
    val categoryName: String? = null, // 카테고리 이름(nullable)
    val date: LocalDateTime // 날짜+시간
)