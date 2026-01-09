package com.example.moneyapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.ui.theme.MainBlue
import com.example.moneyapp.ui.theme.MainYellow
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

/* 날짜 선택 디알로그 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDatePickerDialog(
    initialDate: Date? = null,
    onDismiss: () -> Unit,
    onConfirm: (Date) -> Unit
) {
    // 다이얼로그 처음 열릴 때 선택해 둘 날짜 millis
    val initialMillis = remember(initialDate) {
        initialDate?.time
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis ?: return@TextButton

                    val selectedDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    // 기존 값이 있으면 날짜만 바꾸고 시간은 유지
                    val resultMillis = run {
                        val zone = ZoneId.systemDefault()
                        val oldTime = initialDate?.let {
                            Instant.ofEpochMilli(it.time)
                                .atZone(zone)
                                .toLocalTime()
                        } ?: LocalTime.MIDNIGHT

                        LocalDateTime.of(selectedDate, oldTime)
                            .atZone(zone)
                            .toInstant()
                            .toEpochMilli()
                    }

                    onConfirm(Date(resultMillis))
                    onDismiss()
                }
            ) {
                Text(
                    text = "확인",
                    color = MainBlack
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "취소",
                    color = Color.Gray
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                todayDateBorderColor = MainYellow,          // 오늘 날짜 테두리 색
                todayContentColor = MainYellow,             // 오늘 날짜 텍스트 색
                selectedDayContainerColor = MainYellow,     // 선택된 날짜 배경색
                selectedDayContentColor = Color.White,      // 선택된 날짜 텍스트 색
                currentYearContentColor = MainYellow,       // 현재 연도 텍스트 색
                selectedYearContainerColor = MainYellow,    // 선택된 연도 배경색
                selectedYearContentColor = Color.White      // 선택된 연도 텍스트 색
            )
        )
    }
}

/* 시간 선택 디알로그 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTimePickerDialog(
    initialDate: Date? = null,
    onDismiss: () -> Unit,
    onConfirm: (Date) -> Unit
) {
    val zone = remember { ZoneId.systemDefault() }

    // initialDate가 있으면 그 값, 없으면 현재
    val baseDateTime = remember(initialDate) {
        (initialDate?.time ?: System.currentTimeMillis())
            .let { millis ->
                Instant.ofEpochMilli(millis).atZone(zone).toLocalDateTime()
            }
    }

    val timePickerState = rememberTimePickerState(
        initialHour = baseDateTime.hour,
        initialMinute = baseDateTime.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    // 날짜 유지하고 시간만 변경
                    val newDateTime = LocalDateTime.of(
                        baseDateTime.toLocalDate(),
                        LocalTime.of(timePickerState.hour, timePickerState.minute)
                    )

                    val resultMillis = newDateTime
                        .atZone(zone)
                        .toInstant()
                        .toEpochMilli()

                    onConfirm(Date(resultMillis))
                    onDismiss()
                }
            ) {
                Text(
                    text = "확인",
                    color = MainBlack
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "취소",
                    color = Color.Gray
                )
            }
        },
        title = {
            Text(
                text = "시간 선택",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        containerColor = Color.White,
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.White,                       // 다이얼 배경 색
                    selectorColor = MainYellow,                         // 시/분 선택 포인트 색
                    timeSelectorSelectedContainerColor = MainBlack,     // 선택된 시/분 숫자 배경 색
                    timeSelectorSelectedContentColor = Color.White,     // 선택된 시/분 숫자 텍스트 색
                    timeSelectorUnselectedContainerColor = Color.White, // 선택되지 않은 시/분 숫자 배경 색
                    timeSelectorUnselectedContentColor = MainBlack      // 선택되지 않은 시/분 숫자 텍스트 색
                )
            )
        }
    )
}