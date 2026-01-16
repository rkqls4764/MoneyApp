package com.example.moneyapp.ui.category.detail

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.ui.category.CategoryViewModel
import com.example.moneyapp.ui.category.edit.CategoryEditEvent
import com.example.moneyapp.ui.components.BasicButton
import com.example.moneyapp.ui.components.BasicDialog
import com.example.moneyapp.ui.components.BasicInfoBar
import com.example.moneyapp.ui.components.DeleteIconTopBar
import com.example.moneyapp.ui.history.edit.HistoryEditEvent

/* 카테고리 상세 화면 */
@Composable
fun CategoryDetailScreen(categoryViewModel: CategoryViewModel) {
    val focusManager = LocalFocusManager.current

    val onEvent = categoryViewModel::onDetailEvent
    val categoryDetailState by categoryViewModel.categoryDetailState.collectAsState()

    var openDelete by remember { mutableStateOf(false) }

    if (openDelete) {
        BasicDialog(
            title = "카테고리를 삭제하시겠습니까?",
            onDismiss = { openDelete = false },
            onClickConfirm = { onEvent(CategoryDetailEvent.ClickedDelete) }
        )
    }

    Scaffold(
        topBar = {
            DeleteIconTopBar(
                title = "카테고리 상세",
                onClickNavIcon = { onEvent(CategoryDetailEvent.ClickedBack) },
                onClickActIcon = { openDelete = true }
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
            CategoryDetailContent(
                categoryInfo = categoryDetailState.categoryInfo
            )

            BasicButton(
                name = "수정하기",
                onClick = {
                    categoryViewModel.onEditEvent(CategoryEditEvent.InitWith(data = categoryDetailState.categoryInfo))
                    onEvent(CategoryDetailEvent.ClickedEdit)
                }
            )
        }
    }
}

/* 카테고리 상세 내용 */
@Composable
private fun CategoryDetailContent(categoryInfo: Category) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BasicInfoBar(
            name = "구분",
            value = if (categoryInfo.type == TransactionType.INCOME) "수입" else "지출"
        )

        BasicInfoBar(
            name = "이름",
            value = categoryInfo.name
        )
    }
}