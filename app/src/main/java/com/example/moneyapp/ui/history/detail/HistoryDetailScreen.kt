package com.example.moneyapp.ui.history.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.ui.components.BasicButton
import com.example.moneyapp.ui.components.BasicDialog
import com.example.moneyapp.ui.components.BasicInfoBar
import com.example.moneyapp.ui.components.DeleteIconTopBar
import com.example.moneyapp.ui.history.HistoryViewModel
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.util.toHmString
import com.example.moneyapp.util.toYmdeString

/* 내역 상세 화면 */
@Composable
fun HistoryDetailScreen(historyViewModel: HistoryViewModel) {
    val focusManager = LocalFocusManager.current

    val onEvent = historyViewModel::onDetailEvent
    val historyDetailState by historyViewModel.historyDetailState.collectAsState()

    var openDelete by remember { mutableStateOf(false) }

    if (openDelete) {
        BasicDialog(
            title = "내역을 삭제하시겠습니까?",
            onDismiss = { openDelete = false },
            onClickConfirm = { onEvent(HistoryDetailEvent.ClickedDelete) }
        )
    }

    Scaffold(
        topBar = {
            DeleteIconTopBar(
                title = "내역 상세",
                onClickNavIcon = { onEvent(HistoryDetailEvent.ClickedBack) },
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
            HistoryDetailContent(
                historyInfo = historyDetailState.historyInfo
            )

            BasicButton(
                name = "수정하기",
                onClick = { onEvent(HistoryDetailEvent.ClickedEdit) }
            )
        }
    }
}

/* 내역 상세 내용 */
@Composable
private fun HistoryDetailContent(historyInfo: MoneyTransaction) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BasicInfoBar(
            name = "구분",
            value = historyInfo.type.name
        )

        BasicInfoBar(
            name = "금액",
            value = historyInfo.amount.toString().format("%,d")
        )

        HorizontalDivider(color = MainBlack.copy(alpha = 0.2f))

        BasicInfoBar(
            name = "날짜",
            value = historyInfo.date.toYmdeString()
        )

        BasicInfoBar(
            name = "시간",
            value = historyInfo.date.toHmString()
        )

        HorizontalDivider(color = MainBlack.copy(alpha = 0.2f))

        BasicInfoBar(
            name = "카테고리",
            value = if (historyInfo.categoryId == null) "없음" else historyInfo.categoryId.toString()
        )

        BasicInfoBar(
            name = "이름",
            value = historyInfo.description
        )

        BasicInfoBar(
            name = "메모",
            value = historyInfo.memo ?: "없음"
        )
    }
}