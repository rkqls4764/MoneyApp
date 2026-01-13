package com.example.moneyapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* 내용이 비어 있을 때 사용 */
@Composable
fun EmptyState(
    text: String
) {
    Box(
        modifier = Modifier.padding(top = 100.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}