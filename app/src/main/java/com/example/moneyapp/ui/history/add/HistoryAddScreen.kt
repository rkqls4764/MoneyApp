package com.example.moneyapp.ui.history.add

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.ui.category.manage.CategoryItem
import com.example.moneyapp.ui.components.BasicBottomSheet
import com.example.moneyapp.ui.components.BasicButton
import com.example.moneyapp.ui.components.BasicDateEditBar
import com.example.moneyapp.ui.components.BasicEditBar
import com.example.moneyapp.ui.components.BasicNumberEditBar
import com.example.moneyapp.ui.components.BasicSearchEditBar
import com.example.moneyapp.ui.components.BasicTimeEditBar
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.components.EmptyState
import com.example.moneyapp.ui.history.HistoryViewModel
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CaptionText
import com.example.moneyapp.ui.theme.MainBlack

/* 내역 추가 화면 */
@Composable
fun HistoryAddScreen(historyViewModel: HistoryViewModel) {
    val focusManager = LocalFocusManager.current

    val onEvent = historyViewModel::onAddEvent
    val historyAddState by historyViewModel.historyAddState.collectAsState()

    LaunchedEffect(Unit) {
        onEvent(HistoryAddEvent.InitFirst)
    }

    Scaffold(
        topBar = {
            BasicTopBar(
                title = "내역 추가",
                onClickNavIcon = { onEvent(HistoryAddEvent.ClickedBack) }
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
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HistoryAddContent(
                historyAddState = historyAddState,
                onEvent = onEvent
            )

            BasicButton(
                name = "추가하기",
                onClick = { onEvent(HistoryAddEvent.ClickedAdd)}
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onEvent(HistoryAddEvent.InitLast)
        }
    }
}

/* 내역 추가 내용 */
@Composable
private fun HistoryAddContent(historyAddState: HistoryAddState, onEvent: (HistoryAddEvent) -> Unit) {
    var openSheet by remember { mutableStateOf(false) }

    if (openSheet) {
        BasicBottomSheet(
            content = {
                CategoryBottomSheetContent(
                    categories = historyAddState.categories.filter { it.type == historyAddState.inputData.transaction.type },
                    onClick = {
                        onEvent(HistoryAddEvent.ChangedCategoryWith(it))
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
            value = historyAddState.inputData.transaction.type,
            onValueChange = { onEvent(HistoryAddEvent.ChangedTypeWith(it)) }
        )

        BasicNumberEditBar(
            name = "금액",
            value = historyAddState.inputData.transaction.amount.toString(),
            onValueChange = { onEvent(HistoryAddEvent.ChangedValueWith(HistoryField.AMOUNT, it)) },
            isRequired = true
        )

        BasicDateEditBar(
            name = "날짜",
            value = historyAddState.inputData.transaction.date,
            onValueChange = { onEvent(HistoryAddEvent.ChangedDateWith(it)) },
            isRequired = true
        )

        BasicTimeEditBar(
            name = "시간",
            value = historyAddState.inputData.transaction.date,
            onValueChange = { onEvent(HistoryAddEvent.ChangedDateWith(it)) },
            isRequired = true
        )

        BasicSearchEditBar(
            name = "카테고리",
            value = historyAddState.selectedCategoryName,
            onClick = { openSheet = true }
        )

        BasicEditBar(
            name = "이름",
            value = historyAddState.inputData.transaction.description,
            onValueChange = { onEvent(HistoryAddEvent.ChangedValueWith(HistoryField.NAME, it)) }
        )

        BasicEditBar(
            name = "메모",
            value = historyAddState.inputData.transaction.memo ?: "",
            onValueChange = { onEvent(HistoryAddEvent.ChangedValueWith(HistoryField.MEMO, it)) }
        )
    }
}

/* 카테고리 선택 바텀 시트 내용 */
@Composable
fun CategoryBottomSheetContent(
    categories: List<Category>,
    onClick: (Category) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (categories.isEmpty()) {
            item {
                EmptyState(
                    text = "카테고리가 없습니다"
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

/* 내역 종류 수정 바 */
@Composable
fun EditTypeBar(
    value: TransactionType,
    onValueChange: (TransactionType) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append("구분 ")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("*")
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        TypeSelectorItem(
            selected = value,
            onSelected = { onValueChange(it) }
        )
    }
}

/* 내역 종류 선택 아이템 */
@Composable
fun TypeSelectorItem(
    selected: TransactionType,
    onSelected: (TransactionType) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(width = 1.dp, color = MainBlack.copy(alpha = 0.5f), shape = RoundedCornerShape(percent = 50))
    ) {
        val halfWidth = maxWidth / 2

        // 배경색 서서히 바꾸기
        val indicatorX by animateDpAsState(
            targetValue = if (selected == TransactionType.EXPENSE) 0.dp else halfWidth,
            animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
            label = "indicatorX"
        )

        // 좌우로 움직이는 배경
        Box(
            modifier = Modifier
                .offset(x = indicatorX)
                .fillMaxHeight()
                .width(halfWidth)
                .padding(4.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(MainBlack)
        )

        Row(Modifier.fillMaxSize()) {
            SelectorItem(
                text = "지출",
                selected = selected == TransactionType.EXPENSE,
                onClick = { onSelected(TransactionType.EXPENSE) }
            )
            SelectorItem(
                text = "수입",
                selected = selected == TransactionType.INCOME,
                onClick = { onSelected(TransactionType.INCOME) }
            )
        }
    }
}

/* 내역 종류 선택 아이템 */
@Composable
private fun RowScope.SelectorItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    // 글자 색상 서서히 바꾸기
    val animatedColor by animateColorAsState(
        targetValue = if (selected) Color.White else MainBlack,
        animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        label = "textColor"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = BodyText,
            fontWeight = FontWeight.Medium,
            color = animatedColor
        )
    }
}
