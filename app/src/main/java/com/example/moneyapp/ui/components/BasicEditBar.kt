package com.example.moneyapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.moneyapp.ui.theme.CaptionText
import com.example.moneyapp.util.toHmString
import com.example.moneyapp.util.toYmdeString
import java.util.Date
import kotlin.math.max

/* 기본 정보 수정 바 */
@Composable
fun BasicEditBar(
    name: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    isRequired: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(name)
                if (isRequired) {
                    append(" ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        BasicOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { onValueChange(it) }
        )
    }
}

/* 기본 숫자 수정 바 */
@Composable
fun BasicNumberEditBar(
    name: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    isRequired: Boolean = false
) {
    val maxLen = 9 // Long
    val maxValueStr = Long.MAX_VALUE.toString()

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(name)
                if (isRequired) {
                    append(" ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        BasicOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { newValue ->
                // 숫자만 허용
                if (!newValue.matches(Regex("^[0-9]*$"))) {
                    return@BasicOutlinedTextField
                }

                // 길이 19자 제한
                if (newValue.length > maxLen) {
                    return@BasicOutlinedTextField
                }

                // 19자리이면 Long.MAX_VALUE 넘지 않게 제한
                if (newValue.length == maxLen && newValue > maxValueStr) {
                    return@BasicOutlinedTextField
                }

                onValueChange(newValue)
            }
        )
    }
}

/* 기본 날짜 수정 바 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDateEditBar(
    name: String,
    value: Date,
    onValueChange: (Date) -> Unit,
    isRequired: Boolean = false
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    if (openDialog) {
        BasicDatePickerDialog(
            initialDate = value,
            onDismiss = { openDialog = false },
            onConfirm = { onValueChange(it) }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(name)
                if (isRequired) {
                    append(" ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        IconOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.toYmdeString(),
            icon = Icons.Default.CalendarToday,
            onClick = { openDialog = true }
        )
    }
}

/* 기본 시간 수정 바 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTimeEditBar(
    name: String,
    value: Date,
    onValueChange: (Date) -> Unit,
    isRequired: Boolean = false
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    if (openDialog) {
        BasicTimePickerDialog(
            initialDate = value,
            onDismiss = { openDialog = false },
            onConfirm = { onValueChange(it) }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(name)
                if (isRequired) {
                    append(" ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        IconOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.toHmString(),
            icon = Icons.Default.AccessTime,
            onClick = { openDialog = true }
        )
    }
}

/* 기본 드롭다운 수정 바 */
@Composable
fun BasicDropdownEditBar(
    name: String,
    isRequired: Boolean = false,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(name)
                if (isRequired) {
                    append(" ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        BasicDropDownField(
            options = options,
            selected = selected,
            onSelected = { onSelected(it) }
        )
    }
}

/* 기본 검색 바 */
@Composable
fun BasicSearchEditBar(
    name: String,
    value: String,
    onClick: () -> Unit = {},
    isRequired: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(name)
                if (isRequired) {
                    append(" ")
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        IconOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            icon = Icons.Default.Search,
            onClick = { onClick() }
        )
    }
}