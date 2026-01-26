package com.gabeen.moneyapp.ui.home.calendar

import java.time.LocalDate
import java.time.YearMonth

object CalendarReducer {
    fun reduce(s: CalendarState, e: CalendarEvent): CalendarState = when (e) {
        CalendarEvent.ClickedMoveNextMonth -> handleClickedMoveNextMonth(s)
        CalendarEvent.ClickedMovePrevMonth -> handleClickedMovePrevMonth(s)
        CalendarEvent.CloseSheet -> handleCloseSheet(s)
        is CalendarEvent.ClickedDayBlock -> handleClickedDayBlock(s, e.date)
        CalendarEvent.ClickedMoveNextDay -> handleClickedMoveNextDay(s)
        CalendarEvent.ClickedMovePrevDay -> handleClickedMovePrevDay(s)
        CalendarEvent.ClickedHistory -> handleClickedHistory(s)
        else -> s
    }

    private fun handleClickedMoveNextMonth(
        state: CalendarState
    ): CalendarState {
        val newYearMonth = state.yearMonth.plusMonths(1)

        if (newYearMonth.year !in 2021..2100) return state

        return state.copy(yearMonth = newYearMonth)
    }

    private fun handleClickedMovePrevMonth(
        state: CalendarState
    ): CalendarState {
        val newYearMonth = state.yearMonth.minusMonths(1)

        if (newYearMonth.year !in 2021..2100) return state

        return state.copy(yearMonth = newYearMonth)
    }

    private fun handleCloseSheet(
        state: CalendarState
    ): CalendarState {
        return state.copy(openSheet = false)
    }

    private fun handleClickedDayBlock(
        state: CalendarState,
        date: LocalDate
    ): CalendarState {
        return state.copy(selectedDate = date, openSheet = true)
    }

    private fun handleClickedMoveNextDay(
        state: CalendarState
    ): CalendarState {
        val newDate = state.selectedDate.plusDays(1)
        val newYearMonth = YearMonth.from(newDate)

        return state.copy(yearMonth = if (state.yearMonth != newYearMonth) newYearMonth else state.yearMonth, selectedDate = newDate)
    }

    private fun handleClickedMovePrevDay(
        state: CalendarState
    ): CalendarState {
        val newDate = state.selectedDate.minusDays(1)
        val newYearMonth = YearMonth.from(newDate)

        return state.copy(yearMonth = if (state.yearMonth != newYearMonth) newYearMonth else state.yearMonth, selectedDate = newDate)
    }

    private fun handleClickedHistory(
        state: CalendarState
    ): CalendarState {
        return state.copy(openSheet = false)
    }
}