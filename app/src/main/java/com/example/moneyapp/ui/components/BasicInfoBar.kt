package com.example.moneyapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CaptionText

/* 기본 정보 출력 바 */
@Composable
fun BasicInfoBar(
    name: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = name,
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

        Text(
            text = value,
            fontSize = BodyText
        )
    }
}