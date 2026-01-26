package com.gabeen.moneyapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/* 기본 바텀 시트 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicBottomSheet(
    content: @Composable () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 10.dp)
        ) {
            content()
        }
    }
}

/* 크기 맞춤 바텀 시트 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WrapBottomSheet(
    content: @Composable () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.wrapContentHeight(),
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 10.dp)
        ) {
            content()
        }
    }
}