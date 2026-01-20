package com.example.moneyapp.ui.category.manage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.ui.category.CategoryViewModel
import com.example.moneyapp.ui.category.detail.CategoryDetailEvent
import com.example.moneyapp.ui.components.BasicFloatingButton
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.components.EmptyState
import com.example.moneyapp.ui.history.add.TypeSelectorItem
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.MainBlack

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
        },
        floatingActionButton = {
            BasicFloatingButton(
                onClick = { onEvent(CategoryManageEvent.ClickedAdd) }
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
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
        ) {
            TypeSelectorItem(
                selected = categoryManageState.type,
                onSelected = { onEvent(CategoryManageEvent.ChangedTypeWith(it)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            CategoryListContent(
                categories = categoryManageState.categories.filter { it.type == categoryManageState.type },
                onClick = {
                    onEvent(CategoryManageEvent.ClickedCategory)
                    categoryViewModel.onDetailEvent(CategoryDetailEvent.InitWith(it))
                }
            )
        }
    }
}

/* 카테고리 목록 내용 */
@Composable
private fun CategoryListContent(categories: List<Category>, onClick: (Category) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (categories.isEmpty()) {
            item {
                EmptyState(
                    text = "등록된 카테고리가 없습니다"
                )
            }
        } else {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onClick = { onClick(category) }
                )
            }
        }
    }
}

/* 카테고리 목록 아이템 */
@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 0.5.dp, color = Color.LightGray),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(vertical = 14.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category.name,
                color = MainBlack,
                fontSize = BodyText
            )
        }
    }
}