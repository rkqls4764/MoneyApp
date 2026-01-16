package com.example.moneyapp.ui.category.manage

import com.example.moneyapp.data.entity.TransactionType

sealed interface CategoryManageEvent {
    // 초기화
    data object Init: CategoryManageEvent

    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: CategoryManageEvent

    // 카테고리 추가 버튼 클릭 이벤트
    data object ClickedAdd: CategoryManageEvent

    // 카테고리 분류 값 변경 이벤트
    data class ChangedTypeWith(
        val type: TransactionType
    ): CategoryManageEvent

    // 카테고리 아이템 클릭 이벤트
    data object ClickedCategory: CategoryManageEvent
}