package com.example.moneyapp.ui.home.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyapp.data.repository.MoneyRepository
import com.example.moneyapp.ui.effect.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val moneyRepository: MoneyRepository) : ViewModel() {
    companion object {
        private const val TAG = "CalendarViewModel"
    }

    private val _uiEffect = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val _calendarState = MutableStateFlow(CalendarState())
    val calendarState = _calendarState.asStateFlow()

    fun onEvent(e: CalendarEvent) {
        _calendarState.update { CalendarReducer.reduce(it, e) }

        when (e) {
            else -> Unit
        }
    }

    /* 한 달 정보 조회 */
    fun getMonthData(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch { 
            moneyRepository.getCalendarData(
                startDate = startDate,
                endDate = endDate
            ).collect { data ->


                Log.d(TAG, "[getMonthData] 한 달 정보 조회 성공 (${startDate}~${endDate})\n${data}")
            }
        }
    }
}