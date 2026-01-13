package com.example.moneyapp.ui.category.manage

sealed interface CategoryManageEvent {
    // 초기화
    data object Init: CategoryManageEvent

    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: CategoryManageEvent
}