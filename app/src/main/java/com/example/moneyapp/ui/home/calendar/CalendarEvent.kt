package com.example.moneyapp.ui.home.calendar

import java.time.LocalDate

sealed interface CalendarEvent {
    /* 다음 월 이동 버튼 클릭 이벤트 */
    data object ClickedMoveNextMonth: CalendarEvent

    /* 이전 월 이동 버튼 클릭 이벤트 */
    data object ClickedMovePrevMonth: CalendarEvent

    /* 내역 목록 바텀 시트 닫기 이벤트 */
    data object CloseSheet: CalendarEvent

    /* 날짜 블럭 클릭 이벤트 */
    data class ClickedDayBlock(
        val date: LocalDate
    ): CalendarEvent

    /* 다음 일 이동 버튼 클릭 이벤트 */
    data object ClickedMoveNextDay: CalendarEvent

    /* 이전 일 이동 버튼 클릭 이벤트 */
    data object ClickedMovePrevDay: CalendarEvent

    /* 내역 아이템 클릭 이벤트 */
    data object ClickedHistory: CalendarEvent
}