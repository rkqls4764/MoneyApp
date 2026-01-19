package com.example.moneyapp.ui.home.statistic

import androidx.compose.ui.graphics.Color
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import com.example.moneyapp.util.toYmDisplayString
import com.example.moneyapp.util.toYmString
import java.time.LocalDateTime

data class StatisticState(
//    val expenseData: Map<Category, Int> = emptyMap(),           // 원 그래프 데이터 - 지출 (카테고리, 비율(%))
//    val incomeData: Map<Category, Int> = emptyMap(),            // 원 그래프 데이터 - 수입
    val expenseData: Map<Category, Int> = mapOf(
        Category(id = 1, name = "식비", type = TransactionType.EXPENSE) to 28,
        Category(id = 2, name = "주거/관리비", type = TransactionType.EXPENSE) to 22,
        Category(id = 3, name = "교통", type = TransactionType.EXPENSE) to 10,
        Category(id = 4, name = "쇼핑", type = TransactionType.EXPENSE) to 12,
        Category(id = 5, name = "여가/문화", type = TransactionType.EXPENSE) to 8,
        Category(id = 6, name = "카페/간식", type = TransactionType.EXPENSE) to 7,
        Category(id = 7, name = "의료/건강", type = TransactionType.EXPENSE) to 5,
        Category(id = 8, name = "통신비", type = TransactionType.EXPENSE) to 4,
        Category(id = 9, name = "교육", type = TransactionType.EXPENSE) to 2,
        Category(id = 10, name = "기타", type = TransactionType.EXPENSE) to 2
    ),
    val incomeData: Map<Category, Int> = mapOf(
        Category(id = 1, name = "식비", type = TransactionType.EXPENSE) to 28,
        Category(id = 2, name = "주거/관리비", type = TransactionType.EXPENSE) to 22,
        Category(id = 3, name = "교통", type = TransactionType.EXPENSE) to 10,
        Category(id = 4, name = "쇼핑", type = TransactionType.EXPENSE) to 12,
        Category(id = 5, name = "여가/문화", type = TransactionType.EXPENSE) to 8,
        Category(id = 6, name = "카페/간식", type = TransactionType.EXPENSE) to 7,
        Category(id = 7, name = "의료/건강", type = TransactionType.EXPENSE) to 5,
        Category(id = 8, name = "통신비", type = TransactionType.EXPENSE) to 4,
        Category(id = 9, name = "교육", type = TransactionType.EXPENSE) to 2,
        Category(id = 10, name = "기타", type = TransactionType.EXPENSE) to 2
    ),
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