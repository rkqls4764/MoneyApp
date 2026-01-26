package com.gabeen.moneyapp.ui.category.edit

import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.TransactionType

sealed interface CategoryEditEvent {
    // 초기화
    data class InitWith(
        val data: Category
    ): CategoryEditEvent

    // 뒤로가기 버튼 클릭 이벤트
    data object ClickedBack: CategoryEditEvent

    // 카테고리 이름 값 변경 이벤트
    data class ChangedNameWith(
        val name: String
    ): CategoryEditEvent

    // 카테고리 구분 값 변경 이벤트
    data class ChangedTypeWith(
        val type: TransactionType
    ): CategoryEditEvent

    // 수정 버튼 클릭 이벤트
    data object ClickedUpdate: CategoryEditEvent
}