package com.example.moneyapp.ui.home.calendar

import java.time.LocalDate

object CalendarReducer {
    fun reduce(s: CalendarState, e: CalendarEvent): CalendarState = when (e) {
        CalendarEvent.ClickedMoveNextMonth -> handleClickedMoveNextMonth(s)
        CalendarEvent.ClickedMovePrevMonth -> handleClickedMovePrevMonth(s)
        CalendarEvent.CloseSheet -> handleCloseSheet(s)
        is CalendarEvent.ClickedDayBlock -> handleClickedDayBlock(s, e.date)
        CalendarEvent.ClickedMoveNextDay -> handleClickedMoveNextDay(s)
        CalendarEvent.ClickedMovePrevDay -> handleClickedMovePrevDay(s)
        else -> s
    }

    private fun handleClickedMoveNextMonth(
        state: CalendarState
    ): CalendarState {
        return state.copy(yearMonth = state.yearMonth.plusMonths(1))
    }

    private fun handleClickedMovePrevMonth(
        state: CalendarState
    ): CalendarState {
        return state.copy(yearMonth = state.yearMonth.minusMonths(1))
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
        val day = state.selectedDate.plusDays(1)
        return state.copy(selectedDate = day)
    }

    private fun handleClickedMovePrevDay(
        state: CalendarState
    ): CalendarState {
        val day = state.selectedDate.minusDays(1)
        return state.copy(selectedDate = day)
    }
}