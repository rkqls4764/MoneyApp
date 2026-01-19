package com.example.moneyapp.ui.home.calendar

import com.example.moneyapp.data.entity.TransactionWithCategory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

data class CalendarState(
    val yearMonth: YearMonth = YearMonth.now(),                                         // 출력할 월
    val selectedDate: LocalDate = LocalDate.now(),                                      // 선택한 날짜
    val openSheet: Boolean = false,                                                     // 내역 목록 바텀 시트 열림 여부
    val monthSummary: AmountSummary = AmountSummary(),                                  // 월별 요약
    val dailySummaries: Map<LocalDate, AmountSummary> = emptyMap(),                     // 일별 요약
    val dailyHistories: Map<LocalDate, List<TransactionWithCategory>> = emptyMap()      // 일별 내역 목록
)

data class AmountSummary(
    val total: Long = 0,    // 총 합
    val income: Long = 0,   // 총 수입
    val expense: Long = 0   // 총 지출
)