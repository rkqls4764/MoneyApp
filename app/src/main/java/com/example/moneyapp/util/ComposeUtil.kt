package com.example.moneyapp.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* 뒤로 가기 버튼 한 번만 동작하게 처리 */
@Composable
fun rememberOnce(action: () -> Unit): () -> Unit {
    var done by remember { mutableStateOf(false) }
    return {
        if (!done) {
            done = true
            action()
        }
    }
}

/* 빈 리스트인지 검사 */
@Composable
fun isEmptyList(list: List<Any>, text: String) {
    if (list.isEmpty()) {
        Box(
            modifier = Modifier.padding(top = 100.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp
            )
        }
    }
}
