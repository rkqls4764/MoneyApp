package com.gabeen.moneyapp.ui.home.statistic

import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.TransactionType
import com.gabeen.moneyapp.util.toYDisplayString
import com.gabeen.moneyapp.util.toYmDisplayString
import com.gabeen.moneyapp.util.toYmWeekDisplayString
import com.gabeen.moneyapp.util.toYmdString
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
        is StatisticEvent.ChangedYearWith -> handleChangedYear(s, e.year)
        is StatisticEvent.ChangedMonthWith -> handleChangedMonth(s, e.month)
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

                if (start.year !in 2021..2100) return state

                start to end
            }

            PeriodType.MONTH -> {
                val base = state.query.startDate.plusMonths(1)
                val start = base.withDayOfMonth(1)
                val end = base.withDayOfMonth(base.toLocalDate().lengthOfMonth())

                if (start.year !in 2021..2100) return state

                start to end
            }

            PeriodType.WEEK -> {
                val nextStart = state.query.endDate.plusDays(1)

                val start = nextStart
                val endDay = minOf(nextStart.dayOfMonth + 6, nextStart.toLocalDate().lengthOfMonth())
                val end = nextStart.withDayOfMonth(endDay)

                if (start.year !in 2021..2100) return state

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

                if (start.year !in 2021..2100) return state

                start to end
            }

            PeriodType.MONTH -> {
                val base = state.query.startDate.minusMonths(1)
                val start = base.withDayOfMonth(1)
                val end = base.withDayOfMonth(base.toLocalDate().lengthOfMonth())

                if (start.year !in 2021..2100) return state

                start to end
            }

            PeriodType.WEEK -> {
                val prevBase = state.query.startDate.minusDays(1)

                val day = prevBase.dayOfMonth
                val startDay = ((day - 1) / 7) * 7 + 1
                val endDay = minOf(startDay + 6, prevBase.toLocalDate().lengthOfMonth())

                val start = prevBase.withDayOfMonth(startDay)
                val end = prevBase.withDayOfMonth(endDay)

                if (start.year !in 2021..2100) return state

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
            DateType.START -> state.copy(query = state.query.copy(startDate = value), dateStr = "${value.toYmdString()}  ~  ${state.query.endDate.toYmdString()}")
            DateType.END -> state.copy(query = state.query.copy(endDate = value), dateStr = "${state.query.startDate.toYmdString()}  ~  ${value.toYmdString()}")
        }
    }

    private fun handleChangedYear(
        state: StatisticState,
        year: String
    ): StatisticState {
        val period = state.query.period

        val newStartDate = if (state.query.period == PeriodType.WEEK) {
            state.query.startDate.withYear(year.toInt()).withDayOfMonth(1)
        } else {
            state.query.startDate.withYear(year.toInt())
        }

        val newEndDate = if (state.query.period == PeriodType.WEEK) {
            state.query.endDate.withYear(year.toInt()).withDayOfMonth(7)
        } else {
            state.query.endDate.withYear(year.toInt())
        }

        val newDateStr = when (period) {
            PeriodType.YEAR -> newStartDate.toYDisplayString()
            PeriodType.MONTH -> newStartDate.toYmDisplayString()
            PeriodType.WEEK -> newStartDate.toYmWeekDisplayString()
            PeriodType.CUSTOM -> "${newStartDate.toYmdString()}  ~  ${newEndDate.toYmdString()}"
        }

        return state.copy(query = state.query.copy(startDate = newStartDate, endDate = newEndDate), dateStr = newDateStr)
    }

    private fun handleChangedMonth(
        state: StatisticState,
        month: String
    ): StatisticState {
        val period = state.query.period

        val newStartDate = if (state.query.period == PeriodType.WEEK) {
            state.query.startDate.withMonth(month.toInt()).withDayOfMonth(1)
        } else {
            state.query.startDate.withMonth(month.toInt())
        }

        val newEndDate = if (state.query.period == PeriodType.WEEK) {
            state.query.endDate.withMonth(month.toInt()).withDayOfMonth(7)
        } else {
            state.query.endDate.withMonth(month.toInt())
        }

        val newDateStr = when (period) {
            PeriodType.YEAR -> newStartDate.toYDisplayString()
            PeriodType.MONTH -> newStartDate.toYmDisplayString()
            PeriodType.WEEK -> newStartDate.toYmWeekDisplayString()
            PeriodType.CUSTOM -> "${newStartDate.toYmdString()}  ~  ${newEndDate.toYmdString()}"
        }

        return state.copy(query = state.query.copy(startDate = newStartDate, endDate = newEndDate), dateStr = newDateStr)
    }
}