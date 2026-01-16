package com.example.moneyapp.ui.category.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.moneyapp.ui.category.CategoryViewModel
import com.example.moneyapp.ui.components.BasicButton
import com.example.moneyapp.ui.components.BasicEditBar
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.history.add.EditTypeBar

/* 카테고리 수정 화면 */
@Composable
fun CategoryEditScreen(categoryViewModel: CategoryViewModel) {
    val focusManager = LocalFocusManager.current

    val onEvent = categoryViewModel::onEditEvent
    val categoryEditState by categoryViewModel.categoryEditState.collectAsState()

    Scaffold(
        topBar = {
            BasicTopBar(
                title = "카테고리 수정",
                onClickNavIcon = { onEvent(CategoryEditEvent.ClickedBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(bottom = 10.dp)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryEditContent(
                categoryEditState = categoryEditState,
                onEvent = onEvent
            )

            BasicButton(
                name = "저장하기",
                onClick = { onEvent(CategoryEditEvent.ClickedUpdate) }
            )
        }
    }
}

/* 카테고리 추가 내용 */
@Composable
private fun CategoryEditContent(categoryEditState: CategoryEditState, onEvent: (CategoryEditEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EditTypeBar(
            value = categoryEditState.inputData.type,
            onValueChange = { onEvent(CategoryEditEvent.ChangedTypeWith(it)) }
        )

        BasicEditBar(
            name = "이름",
            value = categoryEditState.inputData.name,
            onValueChange = { onEvent(CategoryEditEvent.ChangedNameWith(it)) }
        )
    }
}
