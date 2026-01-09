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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.moneyapp.ui.components.BasicEditBar
import com.example.moneyapp.ui.components.BasicNumberEditBar
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CaptionText
import com.example.moneyapp.ui.theme.MainBlack

/* 내역 추가 화면 */
@Composable
fun HistoryAddScreen(navController: NavController) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            BasicTopBar(
                title = "내역 추가",
                onClickNavIcon = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
        ) {
            HistoryAddCard()
        }
    }
}

/* 내역 추가 카드 */
@Composable
fun HistoryAddCard() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var type by remember { mutableStateOf(HistoryType.EXPENSE) }
        EditTypeBar(
            name = "구분",
            value = type.name,
            onValueChange = {}
        )

        // 카테고리 (바텀시트) 임시

        BasicEditBar(
            name = "이름",
            value = "",
            onValueChange = {},
            isRequired = true
        )

        BasicNumberEditBar(
            name = "금액",
            value = "",
            onValueChange = {},
            isRequired = true
        )

        // 날짜 시간

        BasicEditBar(
            name = "메모",
            value = "",
            onValueChange = {},
            isRequired = false
        )

    }
}

enum class HistoryType {
    EXPENSE, INCOME
}

/* 내역 종류 수정 바 */
@Composable
private fun EditTypeBar(
    name: String,
    value: String,
    onValueChange: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append(name)
                append(" ")
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("*")
                }
            },
            fontSize = CaptionText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 16.dp, bottom = 6.dp)
        )

        TypeSelectorItem(
            onSelected = {}
        )
    }
}

/* 내역 종류 선택 아이템 */
@Composable
private fun TypeSelectorItem(
    modifier: Modifier = Modifier,
    selected: HistoryType = HistoryType.EXPENSE,
    onSelected: (HistoryType) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(width = 1.dp, color = MainBlack, shape = RoundedCornerShape(percent = 50))
    ) {
        val halfWidth = maxWidth / 2

        // 배경색 서서히 바꾸기
        val indicatorX by animateDpAsState(
            targetValue = if (selected == HistoryType.EXPENSE) 0.dp else halfWidth,
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
                selected = selected == HistoryType.EXPENSE,
                onClick = { onSelected(HistoryType.EXPENSE) }
            )
            SelectorItem(
                text = "수입",
                selected = selected == HistoryType.INCOME,
                onClick = { onSelected(HistoryType.INCOME) }
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
