package com.gabeen.moneyapp.ui.history.detail

import com.gabeen.moneyapp.data.entity.TransactionWithCategory

sealed interface HistoryDetailEvent {
    // 초기화
    data class InitWith(
        val historyInfo: TransactionWithCategory
    ): HistoryDetailEvent

    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: HistoryDetailEvent

    // 수정 버튼 클릭 이벤트
    data object ClickedEdit: HistoryDetailEvent

    // 삭제 버튼 클릭 이벤트
    data object ClickedDelete: HistoryDetailEvent
}