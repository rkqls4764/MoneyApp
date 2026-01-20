package com.example.moneyapp.ui.home.statistic

import androidx.compose.ui.graphics.Color
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import com.example.moneyapp.util.toYmDisplayString
import com.example.moneyapp.util.toYmString
import java.time.LocalDateTime

data class StatisticState(
    val expenseData: Map<Category, Float> = emptyMap(),             // 원 그래프 데이터 - 지출 (카테고리, 비율(%))
    val incomeData: Map<Category, Float> = emptyMap(),              // 원 그래프 데이터 - 수입
    val query: StatisticQuery = StatisticQuery(),                   // 검색 조건
    val dateStr: String = LocalDateTime.now().toYmDisplayString(),  // 출력용 날짜
    val histories: List<TransactionWithCategory> = emptyList()      // 내역 목록
)

data class StatisticQuery(
    val startDate: LocalDateTime = LocalDateTime.now().withDayOfMonth(1),
    val endDate: LocalDateTime = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()),
    val period: PeriodType = PeriodType.MONTH,
    val type: TransactionType = TransactionType.EXPENSE,
    val categoryIds: List<Long> = emptyList()
)

enum class PeriodType(val displayName: String) {
    YEAR("연간"),
    MONTH("월간"),
    WEEK("주간"),
    CUSTOM("사용자 지정");

    companion object {
        fun fromDisplayName(name: String): PeriodType? {
            return PeriodType.entries.find { it.displayName == name }
        }
    }
}