package com.example.moneyapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/* 터치 차단 레이어 */
@Composable
fun BlockTouchOverlay(enabled: Boolean) {
    if (!enabled) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) awaitPointerEvent() // 전부 소비
                }
            }
    )
}
