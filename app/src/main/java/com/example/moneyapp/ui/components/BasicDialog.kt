package com.example.moneyapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
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
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CaptionText
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.ui.theme.MainBlue
import com.example.moneyapp.ui.theme.MainRed
import com.example.moneyapp.ui.theme.MainYellow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/* 기본 디알로그 */
@Composable
fun BasicDialog(
    title: String,
    content: String = "",
    onDismiss: () -> Unit,
    onClickConfirm: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = title,
                fontSize = BodyText,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            if (content.isNotBlank()) {
                Text(
                    text = content,
                    fontSize = CaptionText
                )
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClickConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = "확인",
                    fontSize = BodyText,
                    fontWeight = FontWeight.Bold,
                    color = MainRed
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = "취소",
                    fontSize = BodyText,
                    color = MainBlue
                )
            }
        },
        containerColor = Color.White
    )
}

/* 날짜 선택 디알로그 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDatePickerDialog(
    initialDate: LocalDateTime? = null,
    onDismiss: () -> Unit,
    onConfirm: (LocalDateTime) -> Unit
) {
    val zone = ZoneId.systemDefault()

    // 다이얼로그 처음 열릴 때 선택해 둘 날짜 millis
    val initialMillis = remember(initialDate) {
        initialDate
            ?.atZone(zone)
            ?.toInstant()
            ?.toEpochMilli()
    }

    // 2021-01-01 ~ 2100-12-31 범위만 가능
    val minMillis = remember {
        LocalDate.of(2021, 1, 1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
    }
    val maxMillis = remember {
        LocalDate.of(2100, 12, 31)
            .plusDays(1) // inclusive 처리용(다음날 00:00 미만)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in minMillis until maxMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year in 2021..2100
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                        ?: return@TextButton

                    val selectedDate = Instant.ofEpochMilli(millis)
                        .atZone(zone)
                        .toLocalDate()

                    // 기존 시간이 있으면 유지, 없으면 00:00
                    val resultDateTime = LocalDateTime.of(
                        selectedDate,
                        initialDate?.toLocalTime() ?: LocalTime.MIDNIGHT
                    )

                    onConfirm(resultDateTime)
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
    initialDate: LocalDateTime? = null,
    onDismiss: () -> Unit,
    onConfirm: (LocalDateTime) -> Unit
) {
    // initialDate가 있으면 그 값, 없으면 현재
    val baseDateTime = remember(initialDate) {
        initialDate ?: LocalDateTime.now()
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

                    onConfirm(newDateTime)
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