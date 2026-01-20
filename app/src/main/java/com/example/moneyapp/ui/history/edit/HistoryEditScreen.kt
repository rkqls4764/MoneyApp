package com.example.moneyapp.ui.history.edit

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
import com.example.moneyapp.ui.components.BasicBottomSheet
import com.example.moneyapp.ui.components.BasicButton
import com.example.moneyapp.ui.components.BasicDateEditBar
import com.example.moneyapp.ui.components.BasicEditBar
import com.example.moneyapp.ui.components.BasicNumberEditBar
import com.example.moneyapp.ui.components.BasicSearchEditBar
import com.example.moneyapp.ui.components.BasicTimeEditBar
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.history.HistoryViewModel
import com.example.moneyapp.ui.history.add.CategoryBottomSheetContent
import com.example.moneyapp.ui.history.add.EditTypeBar
import com.example.moneyapp.ui.history.add.HistoryField

/* 내역 수정 화면 */
@Composable
fun HistoryEditScreen(historyViewModel: HistoryViewModel) {
    val focusManager = LocalFocusManager.current

    val onEvent = historyViewModel::onEditEvent
    val historyEditState by historyViewModel.historyEditState.collectAsState()

    Scaffold(
        topBar = {
            BasicTopBar(
                title = "내역 수정",
                onClickNavIcon = { onEvent(HistoryEditEvent.ClickedBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(bottom = 30.dp)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HistoryEditContent(
                historyEditState = historyEditState,
                onEvent = onEvent
            )

            BasicButton(
                name = "저장하기",
                onClick = { onEvent(HistoryEditEvent.ClickedUpdate) }
            )
        }
    }
}

/* 내역 추가 내용 */
@Composable
private fun HistoryEditContent(historyEditState: HistoryEditState, onEvent: (HistoryEditEvent) -> Unit) {
    var openSheet by remember { mutableStateOf(false) }

    if (openSheet) {
        BasicBottomSheet(
            content = {
                CategoryBottomSheetContent(
                    categories = historyEditState.categories.filter { it.type == historyEditState.inputData.transaction.type },
                    onClick = {
                        onEvent(HistoryEditEvent.ChangedCategoryWith(it))
                        openSheet = false
                    }
                )
            },
            onDismiss = { openSheet = false }
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EditTypeBar(
            value = historyEditState.inputData.transaction.type,
            onValueChange = { onEvent(HistoryEditEvent.ChangedTypeWith(it)) }
        )

        BasicNumberEditBar(
            name = "금액",
            value = historyEditState.inputData.transaction.amount.toString(),
            onValueChange = { onEvent(HistoryEditEvent.ChangedValueWith(HistoryField.AMOUNT, it)) },
            isRequired = true
        )

        BasicDateEditBar(
            name = "날짜",
            value = historyEditState.inputData.transaction.date,
            onValueChange = { onEvent(HistoryEditEvent.ChangedDateWith(it)) },
            isRequired = true
        )

        BasicTimeEditBar(
            name = "시간",
            value = historyEditState.inputData.transaction.date,
            onValueChange = { onEvent(HistoryEditEvent.ChangedDateWith(it)) },
            isRequired = true
        )

        BasicSearchEditBar(
            name = "카테고리",
            value = historyEditState.selectedCategoryName,
            onClick = { openSheet = true }
        )

        BasicEditBar(
            name = "이름",
            value = historyEditState.inputData.transaction.description,
            onValueChange = { onEvent(HistoryEditEvent.ChangedValueWith(HistoryField.NAME, it)) }
        )

        BasicEditBar(
            name = "메모",
            value = historyEditState.inputData.transaction.memo ?: "",
            onValueChange = { onEvent(HistoryEditEvent.ChangedValueWith(HistoryField.MEMO, it)) }
        )
    }
}
