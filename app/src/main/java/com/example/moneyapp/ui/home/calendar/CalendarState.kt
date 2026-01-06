package com.example.moneyapp.ui.home.calendar

import java.time.LocalDate
import java.time.YearMonth

data class CalendarState(
    val yearMonth: YearMonth = YearMonth.now(),     // 출력할 월
    val selectedDate: LocalDate = LocalDate.now(),  // 선택한 날짜
    val openSheet: Boolean = false,                 // 내역 목록 바텀 시트 열림 여부
)