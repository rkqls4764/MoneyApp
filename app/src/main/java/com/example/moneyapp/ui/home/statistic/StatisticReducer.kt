package com.example.moneyapp.ui.home.statistic

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.util.toYDisplayString
import com.example.moneyapp.util.toYmDisplayString
import com.example.moneyapp.util.toYmWeekDisplayString
import com.example.moneyapp.util.toYmdString
import java.time.LocalDateTime

object StatisticReducer {
    fun reduce(s: StatisticState, e: StatisticEvent): StatisticState = when (e) {
        is StatisticEvent.ChangedTypeWith -> handleChangedType(s, e.type)
        StatisticEvent.ClickedMoveNext -> handleClickedMoveNext(s)
        StatisticEvent.ClickedMovePrev -> handleClickedMovePrev(s)
        is StatisticEvent.ClickedCategoryWith -> handleClickedCategory(s, e.category)
        is StatisticEvent.SelectedPeriodWith -> handleSelectedPeriod(s, e.period)
        is StatisticEvent.ChangedDateWith -> handleChangedDate(s, e.type, e.value)
        else -> s
    }

    private fun handleChangedType(
        state: StatisticState,
        value: TransactionType
    ): StatisticState {
        return state.copy(query = state.query.copy(type = value))
    }

    private fun handleClickedMoveNext(
        state: StatisticState
    ): StatisticState {
        val newStartDate = when (state.query.period) {
            PeriodType.YEAR -> state.query.startDate.plusYears(1)
            PeriodType.MONTH -> state.query.startDate.plusMonths(1)
            PeriodType.WEEK -> state.query.startDate.plusWeeks(1)
            PeriodType.CUSTOM -> state.query.startDate
        }

        val newEndDate = when (state.query.period) {
            PeriodType.YEAR -> state.query.endDate.plusYears(1)
            PeriodType.MONTH -> state.query.endDate.plusMonths(1)
            PeriodType.WEEK -> state.query.endDate.plusWeeks(1)
            PeriodType.CUSTOM -> state.query.endDate
        }

        val newDateStr = when (state.query.period) {
            PeriodType.YEAR -> newStartDate.toYDisplayString()
            PeriodType.MONTH -> newStartDate.toYmDisplayString()
            PeriodType.WEEK -> newStartDate.toYmWeekDisplayString()
            PeriodType.CUSTOM -> "${newStartDate.toYmdString()} ~ ${newEndDate.toYmdString()}"
        }

        return state.copy(query = state.query.copy(startDate = newStartDate, endDate = newEndDate), dateStr = newDateStr)
    }

    private fun handleClickedMovePrev(
        state: StatisticState
    ): StatisticState {
        val newStartDate = when (state.query.period) {
            PeriodType.YEAR -> {
                state.query.startDate.minusYears(1)
            }
            PeriodType.MONTH -> {
                state.query.startDate.minusMonths(1)
            }
            PeriodType.WEEK -> {
                state.query.startDate.minusWeeks(1)
            }
            PeriodType.CUSTOM -> {
                state.query.startDate
            }
        }

        val newEndDate = when (state.query.period) {
            PeriodType.YEAR -> {
                state.query.endDate.minusYears(1)
            }
            PeriodType.MONTH -> {
                state.query.endDate.minusMonths(1)
            }
            PeriodType.WEEK -> {
                state.query.endDate.minusWeeks(1)
            }
            PeriodType.CUSTOM -> {
                state.query.endDate
            }
        }

        val newDateStr = when (state.query.period) {
            PeriodType.YEAR -> newStartDate.toYDisplayString()
            PeriodType.MONTH -> newStartDate.toYmDisplayString()
            PeriodType.WEEK -> newStartDate.toYmWeekDisplayString()
            PeriodType.CUSTOM -> "${newStartDate.toYmdString()} ~ ${newEndDate.toYmdString()}"
        }

        return state.copy(query = state.query.copy(startDate = newStartDate, endDate = newEndDate), dateStr = newDateStr)
    }

    private fun handleClickedCategory(
        state: StatisticState,
        categoryInfo: Category
    ): StatisticState {
        val categoryIds = listOf(categoryInfo.id)
        return state.copy(query = state.query.copy(categoryIds = categoryIds))
    }

    private fun handleSelectedPeriod(
        state: StatisticState,
        periodName: String
    ): StatisticState {
        val period = PeriodType.fromDisplayName(periodName) ?: PeriodType.CUSTOM

        val newDateStr = when (period) {
            PeriodType.YEAR -> state.query.startDate.toYDisplayString()
            PeriodType.MONTH -> state.query.startDate.toYmDisplayString()
            PeriodType.WEEK -> state.query.startDate.toYmWeekDisplayString()
            PeriodType.CUSTOM -> "${state.query.startDate.toYmdString()}  ~  ${state.query.endDate.toYmdString()}"
        }

        return state.copy(query = state.query.copy(period = period), dateStr = newDateStr)
    }

    private fun handleChangedDate(
        state: StatisticState,
        type: DateType,
        value: LocalDateTime
    ): StatisticState {
        return when (type) {
            DateType.START -> state.copy(query = state.query.copy(startDate = value))
            DateType.END -> state.copy(query = state.query.copy(endDate = value))
        }
    }
}