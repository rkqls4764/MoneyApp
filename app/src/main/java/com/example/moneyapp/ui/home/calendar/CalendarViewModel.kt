package com.example.moneyapp.ui.home.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import java.time.LocalDate
import java.time.LocalDateTime
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
    // LocalDate -> LocalDateTime으로 변경함
    fun getMonthData(startDate: LocalDateTime, endDate: LocalDateTime) {
        viewModelScope.launch { 
            moneyRepository.getCalendarData(
                startDate = startDate,
                endDate = endDate
            ).collect { data ->

                /* 월별 계산 */
                // 월별 총 수입
                val monthIncome = data.filter { it.transaction.type == TransactionType.INCOME }
                    .sumOf{it.transaction.amount}

                // 월별 총 지출
                val monthExpense = data.filter { it.transaction.type == TransactionType.EXPENSE }
                    .sumOf{it.transaction.amount}

                // 월별 합계
                val monthTotal = monthIncome - monthExpense

                /* 월별 계산 */
                // 날짜별로 그룹화
                val groupedMap = data.groupBy { it.transaction.date }

                // 날짜를 key로 한 맵 생성
                // 날짜 : {총수입, 총지출, 합계, [내역 리스트-TransactionWithCategory]}
                val dayDataMap = groupedMap.mapValues { (_, dayList) ->

                    // 일별 총 수입
                    val dayIncome = dayList.filter { it.transaction.type == TransactionType.INCOME }
                        .sumOf { it.transaction.amount }

                    // 일별 총 지출
                    val dayExpense = dayList.filter { it.transaction.type == TransactionType.EXPENSE }
                        .sumOf { it.transaction.amount }

                    // 일별 합계
                    val dayTotal = dayIncome - dayExpense
                }


                Log.d(TAG, "[getMonthData] 한 달 정보 조회 성공 (${startDate}~${endDate})\n${data}")
            }
        }
    }
}