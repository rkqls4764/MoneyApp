package com.example.moneyapp.ui.category.add

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.moneyapp.ui.category.CategoryViewModel
import com.example.moneyapp.ui.components.BasicButton
import com.example.moneyapp.ui.components.BasicEditBar
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.components.BlockTouchOverlay
import com.example.moneyapp.ui.history.add.EditTypeBar

/* 카테고리 추가 화면 */
@Composable
fun CategoryAddScreen(categoryViewModel: CategoryViewModel) {
    val focusManager = LocalFocusManager.current
    var isClosing by remember { mutableStateOf(false) }

    val onEvent = categoryViewModel::onAddEvent
    val categoryAddState by categoryViewModel.categoryAddState.collectAsState()

    Scaffold(
        topBar = {
            BasicTopBar(
                title = "카테고리 추가",
                onClickNavIcon = {
                    isClosing = true
                    onEvent(CategoryAddEvent.ClickedBack)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 30.dp)
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryAddContent(
                    categoryAddState = categoryAddState,
                    onEvent = onEvent
                )

                BasicButton(
                    name = "추가하기",
                    onClick = { onEvent(CategoryAddEvent.ClickedAdd)}
                )
            }

            BlockTouchOverlay(
                enabled = isClosing
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onEvent(CategoryAddEvent.Init)
        }
    }
}

/* 카테고리 추가 내용 */
@Composable
private fun CategoryAddContent(categoryAddState: CategoryAddState, onEvent: (CategoryAddEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EditTypeBar(
            value = categoryAddState.inputData.type,
            onValueChange = { onEvent(CategoryAddEvent.ChangedTypeWith(it)) }
        )

        BasicEditBar(
            name = "이름",
            value = categoryAddState.inputData.name,
            onValueChange = { onEvent(CategoryAddEvent.ChangedNameWith(it)) },
            isRequired = true
        )
    }
}