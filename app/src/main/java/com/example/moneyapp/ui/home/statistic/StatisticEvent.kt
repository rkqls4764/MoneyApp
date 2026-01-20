package com.example.moneyapp.ui.home.statistic

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import java.time.LocalDateTime

enum class DateType {
    START, END
}

sealed interface StatisticEvent {
    /* 초기화 */
    data object Init: StatisticEvent

    /* 분류 값 변경 이벤트 */
    data class ChangedTypeWith(
        val type: TransactionType
    ): StatisticEvent

    /* 다음 월 이동 버튼 클릭 이벤트 */
    data object ClickedMoveNext: StatisticEvent

    /* 이전 월 이동 버튼 클릭 이벤트 */
    data object ClickedMovePrev: StatisticEvent

    /* 카테고리 아이템 클릭 이벤트 */
    data class ClickedCategoryWith(
        val category: Category
    ): StatisticEvent

    /* 기간 선택 이벤트 */
    data class SelectedPeriodWith(
        val period: String
    ): StatisticEvent

    /* 날짜 필드 값 변경 이벤트 */
    data class ChangedDateWith(
        val type: DateType,
        val value: LocalDateTime
    ): StatisticEvent

    /* 필터 초기화 버튼 클릭 이벤트 */
    data object ClickedInitFilter: StatisticEvent
}