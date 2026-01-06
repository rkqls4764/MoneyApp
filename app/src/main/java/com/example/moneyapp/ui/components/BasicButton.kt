package com.example.moneyapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moneyapp.ui.theme.MainBlue
import com.example.moneyapp.ui.theme.MainYellow

/* 기본 플로팅 버튼 */
@Composable
fun BasicFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MainYellow,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "플로팅 버튼",
            modifier = Modifier.size(30.dp)
        )
    }
}
