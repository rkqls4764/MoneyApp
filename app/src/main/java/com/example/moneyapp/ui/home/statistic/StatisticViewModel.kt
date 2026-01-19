package com.example.moneyapp.ui.home.statistic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.repository.MoneyRepository
import com.example.moneyapp.ui.effect.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(private val moneyRepository: MoneyRepository) : ViewModel() {
    companion object {
        private const val TAG = "StatisticViewModel"
    }

    private val _uiEffect = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val _statisticState = MutableStateFlow(StatisticState())
    val statisticState = _statisticState.asStateFlow()

    fun onEvent(e: StatisticEvent) {
        _statisticState.update { StatisticReducer.reduce(it, e) }

        when (e) {
            StatisticEvent.Init -> getCategoryStatistic()
            StatisticEvent.ClickedMoveNext -> getCategoryStatistic()
            StatisticEvent.ClickedMovePrev -> getCategoryStatistic()
            is StatisticEvent.ClickedCategoryWith -> searchHistories()
            else -> Unit
        }
    }

    /* 내역 목록 검색 */
    fun searchHistories() {
        val query = statisticState.value.query

        viewModelScope.launch {
            moneyRepository.search(
                startDate = query.startDate.withHour(0).withMinute(0).withSecond(0),
                endDate = query.endDate.withHour(23).withMinute(59).withSecond(59),
                types = null,
                categoryIds = query.categoryIds,
                keyword = null
            ).collect { data ->
                _statisticState.update { it.copy(histories = data) }

                Log.d(TAG, "[searchHistories] 내역 목록 검색 성공 (기간: ${query.startDate} ~ ${query.endDate} / 카테고리: ${query.categoryIds})\n${data}")
            }
        }
    }

    /* 카테고리별 통계 조회 */
    fun getCategoryStatistic() {
        val query = statisticState.value.query

        viewModelScope.launch {
            moneyRepository.getCategoryStats(
                start = query.startDate.withHour(0).withMinute(0).withSecond(0),
                end = query.endDate.withHour(23).withMinute(59).withSecond(59),
            ).collect { data ->
                val expenseData = data.filter { it.type == TransactionType.EXPENSE }
                val incomeData = data.filter { it.type == TransactionType.INCOME }

                val expenseTotalSum = expenseData.sumOf { it.totalAmount }   // 지출 총합
                val incomeTotalSum = incomeData.sumOf { it.totalAmount }     // 수입 총합

                val newExpenseMap: Map<Category, Int> = if (expenseTotalSum == 0L) emptyMap() else {
                    expenseData.associate { stat ->
                        val percent = ((stat.totalAmount.toDouble() / expenseTotalSum) * 100).toInt()

                        Category(
                            id = stat.categoryId,
                            name = stat.categoryName,
                            type = stat.type
                        ) to percent
                    }
                }
                val newIncomeMap: Map<Category, Int> = if (incomeTotalSum == 0L) emptyMap() else {
                    incomeData.associate { stat ->
                        val percent = ((stat.totalAmount.toDouble() / incomeTotalSum) * 100).toInt()

                        Category(
                            id = stat.categoryId,
                            name = stat.categoryName,
                            type = stat.type
                        ) to percent
                    }
                }

                _statisticState.update { it.copy(expenseData = newExpenseMap, incomeData = newIncomeMap) }

                Log.d(TAG, "[getCategoryStatistic] 카테고리별 통계 조회 성공 (기간: ${query.startDate} ~ ${query.endDate})\n${data}")
            }
        }
    }
}