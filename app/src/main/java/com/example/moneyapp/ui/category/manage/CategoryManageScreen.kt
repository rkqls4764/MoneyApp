package com.example.moneyapp.ui.category.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.moneyapp.ui.components.BasicDialog
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.components.DeleteIconTopBar
import com.example.moneyapp.ui.history.detail.HistoryDetailEvent

/* 카테고리 관리 화면 */
@Composable
fun CategoryManageScreen(categoryViewModel: CategoryViewModel) {
    val focusManager = LocalFocusManager.current

    val onEvent = categoryViewModel::onManageEvent
    val categoryManageState by categoryViewModel.categoryManageState.collectAsState()

    LaunchedEffect(Unit) {
        onEvent(CategoryManageEvent.Init)
    }

    Scaffold(
        topBar = {
            BasicTopBar(
                title = "카테고리 관리",
                onClickNavIcon = { onEvent(CategoryManageEvent.ClickedBack) }
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

        }
    }
}

