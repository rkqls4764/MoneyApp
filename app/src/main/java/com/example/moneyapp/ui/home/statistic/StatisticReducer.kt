package com.example.moneyapp.ui.home.statistic

import android.util.Log
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
        StatisticEvent.ClickedInitFilter -> StatisticState()
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
        val period = state.query.period

        val (newStartDate, newEndDate) = when (period) {
            PeriodType.YEAR -> {
                val base = state.query.startDate.plusYears(1)
                val start = base.withDayOfYear(1)
                val end = base.withMonth(12).withDayOfMonth(31)
                start to end
            }

            PeriodType.MONTH -> {
                val base = state.query.startDate.plusMonths(1)
                val start = base.withDayOfMonth(1)
                val end = base.withDayOfMonth(base.toLocalDate().lengthOfMonth())
                start to end
            }

            PeriodType.WEEK -> {
                val nextStart = state.query.endDate.plusDays(1)

                val start = nextStart
                val endDay = minOf(nextStart.dayOfMonth + 6, nextStart.toLocalDate().lengthOfMonth())
                val end = nextStart.withDayOfMonth(endDay)

                start to end
            }

            PeriodType.CUSTOM -> state.query.startDate to state.query.endDate
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
        val period = state.query.period

        val (newStartDate, newEndDate) = when (period) {
            PeriodType.YEAR -> {
                val base = state.query.startDate.minusYears(1)
                val start = base.withDayOfYear(1)
                val end = base.withMonth(12).withDayOfMonth(31)
                start to end
            }

            PeriodType.MONTH -> {
                val base = state.query.startDate.minusMonths(1)
                val start = base.withDayOfMonth(1)
                val end = base.withDayOfMonth(base.toLocalDate().lengthOfMonth())
                start to end
            }

            PeriodType.WEEK -> {
                val prevBase = state.query.startDate.minusDays(1)

                val day = prevBase.dayOfMonth
                val startDay = ((day - 1) / 7) * 7 + 1
                val endDay = minOf(startDay + 6, prevBase.toLocalDate().lengthOfMonth())

                val start = prevBase.withDayOfMonth(startDay)
                val end = prevBase.withDayOfMonth(endDay)

                start to end
            }

            PeriodType.CUSTOM -> state.query.startDate to state.query.endDate
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
        val categoryIds = listOf(if (categoryInfo.id == 0.toLong()) -1L else categoryInfo.id)
        return state.copy(query = state.query.copy(categoryIds = categoryIds))
    }

    private fun handleSelectedPeriod(
        state: StatisticState,
        periodName: String
    ): StatisticState {
        val period = PeriodType.fromDisplayName(periodName) ?: PeriodType.CUSTOM
        val baseDate = state.query.startDate

        val (newStartDate, newEndDate) = when (period) {
            PeriodType.YEAR -> {
                val start = baseDate.withDayOfYear(1)
                val end = baseDate.withMonth(12).withDayOfMonth(31)
                start to end
            }
            PeriodType.MONTH -> {
                val start = baseDate.withDayOfMonth(1)
                val end = baseDate.withDayOfMonth(baseDate.toLocalDate().lengthOfMonth())
                start to end
            }
            PeriodType.WEEK -> {
                val day = baseDate.dayOfMonth
                val startDay = ((day - 1) / 7) * 7 + 1
                val endDay = minOf(startDay + 6, baseDate.toLocalDate().lengthOfMonth())

                val start = baseDate.withDayOfMonth(startDay)
                val end = baseDate.withDayOfMonth(endDay)

                start to end
            }
            PeriodType.CUSTOM -> {
                state.query.startDate to state.query.endDate
            }
        }

        val newDateStr = when (period) {
            PeriodType.YEAR -> newStartDate.toYDisplayString()
            PeriodType.MONTH -> newStartDate.toYmDisplayString()
            PeriodType.WEEK -> newStartDate.toYmWeekDisplayString()
            PeriodType.CUSTOM -> "${newStartDate.toYmdString()}  ~  ${newEndDate.toYmdString()}"
        }

        return state.copy(query = state.query.copy(period = period, startDate = newStartDate, endDate = newEndDate), dateStr = newDateStr)
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