package com.example.moneyapp.ui.home.statistic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import com.example.moneyapp.ui.components.BasicBottomSheet
import com.example.moneyapp.ui.components.BasicDateEditBar
import com.example.moneyapp.ui.components.BasicDropdownEditBar
import com.example.moneyapp.ui.components.BasicTextButton
import com.example.moneyapp.ui.components.DetailsPieChart
import com.example.moneyapp.ui.components.EmptyState
import com.example.moneyapp.ui.components.PieChart
import com.example.moneyapp.ui.components.WrapBottomSheet
import com.example.moneyapp.ui.history.add.TypeSelectorItem
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CaptionText
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.ui.theme.MainBlue
import com.example.moneyapp.ui.theme.MainRed
import com.example.moneyapp.ui.theme.PieChartColor.expenseColors
import com.example.moneyapp.ui.theme.PieChartColor.incomeColors
import com.example.moneyapp.ui.theme.TitleText
import com.example.moneyapp.util.formatMoney
import com.example.moneyapp.util.toHmString
import com.example.moneyapp.util.toYmdHmString

/* 통계 화면 */
@Composable
fun StatisticScreen(statisticViewModel: StatisticViewModel, isFilterClicked: Boolean, onDismiss: () -> Unit) {
    val onEvent = statisticViewModel::onEvent
    val statisticState by statisticViewModel.statisticState.collectAsState()

    val pieData = if (statisticState.query.type == TransactionType.EXPENSE) statisticState.expenseData.values.toList() else statisticState.incomeData.values.toList()   // 출력할 원 그래프 데이터

    var openSheet by remember { mutableStateOf(false) }

    if (openSheet) {
        BasicBottomSheet(
            content = {
                HistoriesBottomSheetContent(
                    histories = statisticState.histories.filter { it.transaction.type == statisticState.query.type },
                    onClick = {

                        openSheet = false
                    }
                )
            },
            onDismiss = { openSheet = false }
        )
    }

    if (isFilterClicked) {
        WrapBottomSheet(
            content = {
                FilterBottomSheetContent(
                    statisticState = statisticState,
                    onEvent = onEvent
                )
            },
            onDismiss = { onDismiss() }
        )
    }

    LaunchedEffect(Unit) {
        onEvent(StatisticEvent.Init)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (statisticState.query.period == PeriodType.CUSTOM) {
            Row(
                modifier = Modifier.height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = statisticState.dateStr,
                    fontSize = TitleText,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            YearMonthBar(
                ymStr = statisticState.dateStr,
                onEvent = onEvent
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        TypeSelectorItem(
            selected = statisticState.query.type,
            onSelected = { onEvent(StatisticEvent.ChangedTypeWith(it)) }
        )

        if (pieData.isEmpty()) {
            EmptyState(
                text = "데이터가 없습니다"
            )
        } else {
            Column(
                modifier = Modifier.padding(bottom = 10.dp).verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                PieChart(
                    data = if (statisticState.query.type == TransactionType.EXPENSE) statisticState.expenseData.values.toList() else statisticState.incomeData.values.toList(),
                    colors = if (statisticState.query.type == TransactionType.EXPENSE) expenseColors else incomeColors,
                    radiusOuter = 120.dp,
                    chartBarWidth = 120.dp
                )

                Spacer(modifier = Modifier.height(30.dp))

                DetailsPieChart(
                    data = if (statisticState.query.type == TransactionType.EXPENSE) statisticState.expenseData else statisticState.incomeData,
                    colors = if (statisticState.query.type == TransactionType.EXPENSE) expenseColors else incomeColors,
                    onClick = {
                        openSheet = true
                        onEvent(StatisticEvent.ClickedCategoryWith(it))
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

/* 년,월 출력 바 */
@Composable
private fun YearMonthBar(ymStr: String, onEvent: (StatisticEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(color = MainBlack),
            onClick = { onEvent(StatisticEvent.ClickedMovePrev) }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "이전 월 이동 버튼",
                tint = Color.White
            )
        }

        Text(
            text = ymStr,
            fontSize = TitleText,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(color = MainBlack),
            onClick = { onEvent(StatisticEvent.ClickedMoveNext) }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "다음 월 이동 버튼",
                tint = Color.White
            )
        }
    }
}

/* 카테고리 내역 목록 바텀 시트 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoriesBottomSheetContent(histories: List<TransactionWithCategory>?, onClick: (TransactionWithCategory) -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (histories.isNullOrEmpty()) {
                item {
                    EmptyState(
                        text = "등록된 내역이 없습니다"
                    )
                }
            } else {
                items(histories) { history ->
                    HistoryItem(
                        history = history,
                        onClick = { onClick(history) }
                    )
                }
            }
        }
    }
}

/* 내역 목록 아이템 */
@Composable
private fun HistoryItem(history: TransactionWithCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 0.5.dp, color = Color.LightGray),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(vertical = 18.dp, horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (history.category == null) history.transaction.description else "[${history.category.name}]  ${history.transaction.description}",
                    color = MainBlack,
                    fontSize = BodyText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(0.55f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Text(
                    text = if (history.transaction.type == TransactionType.INCOME) "+ ${formatMoney(history.transaction.amount)} 원" else "- ${formatMoney(history.transaction.amount)} 원",
                    color = if (history.transaction.type == TransactionType.INCOME) MainBlue else MainRed,
                    fontSize = BodyText,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.4f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = history.transaction.date.toYmdHmString(),
                color = MainBlack.copy(alpha = 0.5f),
                fontSize = CaptionText,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/* 검색 기간 설정 바텀 시트 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheetContent(statisticState: StatisticState, onEvent: (StatisticEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BasicDropdownEditBar(
            name = "기간",
            options = PeriodType.entries.map { it.displayName },
            selected = statisticState.query.period.displayName,
            onSelected = { onEvent(StatisticEvent.SelectedPeriodWith(it)) }
        )

        BasicDateEditBar(
            name = "검색 시작 날짜",
            value = statisticState.query.startDate,
            onValueChange = { onEvent(StatisticEvent.ChangedDateWith(DateType.START, it)) },
            enabled = if (statisticState.query.period == PeriodType.CUSTOM) true else false
        )

        BasicDateEditBar(
            name = "검색 종료 날짜",
            value = statisticState.query.endDate,
            onValueChange = { onEvent(StatisticEvent.ChangedDateWith(DateType.END, it)) },
            enabled = if (statisticState.query.period == PeriodType.CUSTOM) true else false
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp, end = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            BasicTextButton(
                name = "초기화",
                onClick = { onEvent(StatisticEvent.ClickedInitFilter) }
            )
        }
    }
}
