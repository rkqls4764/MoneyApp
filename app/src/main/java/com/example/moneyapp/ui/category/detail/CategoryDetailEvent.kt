package com.example.moneyapp.ui.category.detail

import com.example.moneyapp.data.entity.Category

sealed interface CategoryDetailEvent {
    // 초기화
    data class InitWith(
        val categoryInfo: Category
    ): CategoryDetailEvent

    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: CategoryDetailEvent

    // 수정 버튼 클릭 이벤트
    data object ClickedEdit: CategoryDetailEvent

    // 삭제 버튼 클릭 이벤트
    data object ClickedDelete: CategoryDetailEvent
}