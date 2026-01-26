package com.gabeen.moneyapp.ui.category.add

import com.gabeen.moneyapp.data.entity.TransactionType

sealed interface CategoryAddEvent {
    // 초기화
    data object Init: CategoryAddEvent

    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: CategoryAddEvent

    // 카테고리 이름 값 변경 이벤트
    data class ChangedNameWith(
        val name: String
    ): CategoryAddEvent

    // 카테고리 구분 값 변경 이벤트
    data class ChangedTypeWith(
        val type: TransactionType
    ): CategoryAddEvent

    // 카테고리 추가 버튼 클릭 이벤트
    data object ClickedAdd: CategoryAddEvent
}