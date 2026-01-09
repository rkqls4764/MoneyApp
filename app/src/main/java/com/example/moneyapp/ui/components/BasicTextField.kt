package com.example.moneyapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.ui.theme.MainYellow

/* 기본 텍스트 필드 */
@Composable
fun BasicOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = ""
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        shape = RoundedCornerShape(percent = 50),
        colors = BasicOutlinedTextFieldColors(),
        placeholder = {
            Text(
                text = value.ifBlank { hint },
                fontSize = BodyText
            )
        }
    )
}

/* 기본 텍스트 필드 색상 */
@Composable
private fun BasicOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color.White,
    focusedContainerColor = Color.White,
    unfocusedBorderColor = MainBlack,
    focusedBorderColor = MainBlack,
    cursorColor = MainBlack
)