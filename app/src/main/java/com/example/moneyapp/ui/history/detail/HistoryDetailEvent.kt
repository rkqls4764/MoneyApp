package com.example.moneyapp.ui.history.detail

sealed interface HistoryDetailEvent {
    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: HistoryDetailEvent

    // 수정 버튼 클릭 이벤트
    data object ClickedEdit: HistoryDetailEvent

    // 삭제 버튼 클릭 이벤트
    data object ClickedDelete: HistoryDetailEvent
}