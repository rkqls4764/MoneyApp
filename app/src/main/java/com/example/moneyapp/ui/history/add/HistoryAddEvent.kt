package com.example.moneyapp.ui.history.add

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import java.time.LocalDateTime
import java.util.Date

enum class HistoryAddField {
    NAME, AMOUNT, MEMO
}

sealed interface HistoryAddEvent {
    // 내역 필드 값 변경 이벤트
    data class ChangedValueWith(
        val field: HistoryAddField,
        val value: String
    ): HistoryAddEvent

    // 내역 구분 값 변경 이벤트
    data class ChangedTypeWith(
        val type: TransactionType
    ): HistoryAddEvent

    // 날짜, 시간 값 변경 이벤트
    data class ChangedDateWith(
        val date: LocalDateTime
    ): HistoryAddEvent

    // 카테고리 값 변경 이벤트
    data class ChangedCategoryWith(
        val category: Category
    ): HistoryAddEvent

    // 내역 추가 버튼 클릭 이벤트
    data object ClickedAdd: HistoryAddEvent
}