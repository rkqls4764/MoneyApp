package com.gabeen.moneyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import com.gabeen.moneyapp.ui.theme.BodyText
import com.gabeen.moneyapp.ui.theme.MainBlack

/* 기본 텍스트 필드 */
@Composable
fun BasicOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "",
    isNumber: Boolean = false
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
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isNumber) KeyboardType.Number else KeyboardType.Text
        )
    )
}

/* 아이콘 텍스트 필드 */
@Composable
fun IconOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = {},
            singleLine = true,
            readOnly = true,
            shape = RoundedCornerShape(percent = 50),
            colors = BasicOutlinedTextFieldColors(),
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "캘린더 열기"
                )
            },
            enabled = enabled
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { if (enabled) onClick() }
        )
    }
}

/* 기본 텍스트 필드 색상 */
@Composable
fun BasicOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color.White,
    focusedContainerColor = Color.White,
    unfocusedBorderColor = MainBlack.copy(alpha = 0.5f),
    focusedBorderColor = MainBlack.copy(alpha = 0.5f),
    cursorColor = MainBlack.copy(alpha = 0.5f),
    disabledContainerColor = MainBlack.copy(alpha = 0.2f)
)