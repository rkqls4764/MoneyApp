package com.example.moneyapp.ui.history.edit

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import com.example.moneyapp.ui.history.add.HistoryField
import java.time.LocalDateTime

sealed interface HistoryEditEvent {
    // 초기화
    data class InitWith(
        val data: TransactionWithCategory
    ): HistoryEditEvent

    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: HistoryEditEvent

    // 내역 필드 값 변경 이벤트
    data class ChangedValueWith(
        val field: HistoryField,
        val value: String
    ): HistoryEditEvent

    // 내역 구분 값 변경 이벤트
    data class ChangedTypeWith(
        val type: TransactionType
    ): HistoryEditEvent

    // 날짜, 시간 값 변경 이벤트
    data class ChangedDateWith(
        val date: LocalDateTime
    ): HistoryEditEvent

    // 카테고리 값 변경 이벤트
    data class ChangedCategoryWith(
        val category: Category?
    ): HistoryEditEvent

    // 수정 버튼 클릭 이벤트
    data object ClickedUpdate: HistoryEditEvent
}