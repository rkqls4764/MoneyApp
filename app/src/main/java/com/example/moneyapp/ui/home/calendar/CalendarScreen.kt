package com.example.moneyapp.ui.home.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.moneyapp.ui.components.EmptyState
import com.example.moneyapp.ui.history.HistoryViewModel
import com.example.moneyapp.ui.history.detail.HistoryDetailEvent
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CalendarText
import com.example.moneyapp.ui.theme.CaptionText
import com.example.moneyapp.ui.theme.EmptyDayBlockColor
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.ui.theme.MainBlue
import com.example.moneyapp.ui.theme.MainRed
import com.example.moneyapp.ui.theme.TitleText
import com.example.moneyapp.ui.theme.TodayBlockColor
import com.example.moneyapp.util.formatMoney
import com.example.moneyapp.util.toHmString
import com.example.moneyapp.util.toYmString
import java.time.DayOfWeek
import java.time.LocalDate

/* 캘린더 화면 */
@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel, historyViewModel: HistoryViewModel) {
    val onEvent = calendarViewModel::onEvent
    val calendarState by calendarViewModel.calendarState.collectAsState()

    if (calendarState.openSheet) {
        HistoriesBottomSheet(
            histories = calendarState.dailyHistories[calendarState.selectedDate],
            summary = calendarState.dailySummaries[calendarState.selectedDate],
            date = calendarState.selectedDate,
            onClick = {
                onEvent(CalendarEvent.ClickedHistory)
                historyViewModel.onDetailEvent(HistoryDetailEvent.InitWith(it))
            },
            onEvent = onEvent
        )
    }

    Column(
        modifier = Modifier.padding(bottom = 80.dp)
    ) {
        Calendar(
            calendarState = calendarState,
            onEvent = onEvent
        )
    }
}

/* 내역 목록 바텀 시트 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoriesBottomSheet(histories: List<TransactionWithCategory>?, summary: AmountSummary?, date: LocalDate, onClick: (TransactionWithCategory) -> Unit, onEvent: (CalendarEvent) -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = { onEvent(CalendarEvent.CloseSheet) },
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            DayBar(
                date = date,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(10.dp))

            DailySummaryBar(
                total = summary?.total ?: 0,
                income = summary?.income ?: 0,
                expense = summary?.expense ?: 0
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
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
                text = history.transaction.date.toHmString(),
                color = MainBlack.copy(alpha = 0.5f),
                fontSize = CaptionText,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/* 일 출력 바 */
@Composable
private fun DayBar(date: LocalDate, onEvent: (CalendarEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onEvent(CalendarEvent.ClickedMovePrevDay) }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "이전 일 이동 버튼",
                tint = MainBlack
            )
        }

        Text(
            text = "${date.dayOfMonth}일",
            fontSize = TitleText,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = { onEvent(CalendarEvent.ClickedMoveNextDay) }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "다음 일 이동 버튼",
                tint = MainBlack
            )
        }
    }
}

/* 일 요약 바 */
@Composable
private fun DailySummaryBar(total: Long, income: Long, expense: Long) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryItem(
            name = "총합",
            price = total,
            color = MainBlack
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = Color.LightGray
        )

        SummaryItem(
            name = "수입",
            price = income,
            color = MainBlue
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = Color.LightGray
        )

        SummaryItem(
            name = "지출",
            price = expense,
            color = MainRed
        )
    }
}

/* 월 요약 바 */
@Composable
private fun MonthlySummaryBar(total: Long, income: Long, expense: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 8.dp)
            .border(color = Color.LightGray, width = 1.dp, shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryItem(
            name = "총합",
            price = total,
            color = MainBlack
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = Color.LightGray
        )

        SummaryItem(
            name = "수입",
            price = income,
            color = MainBlue
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            color = Color.LightGray
        )

        SummaryItem(
            name = "지출",
            price = expense,
            color = MainRed
        )
    }
}

/* 요약 바 아이템 */
@Composable
private fun RowScope.SummaryItem(name: String, price: Long, color: Color) {
    Box(
        modifier = Modifier.weight(1f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                fontSize = CaptionText,
                color = MainBlack
            )

            Text(
                text = formatMoney(price),
                fontSize = if (formatMoney(price).length > 15) CalendarText else CaptionText,
                color = color,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(state = rememberScrollState()) // 길이가 길면 좌우 스크롤
            )
        }
    }
}

/* 캘린더 */
@Composable
private fun Calendar(calendarState: CalendarState, onEvent: (CalendarEvent) -> Unit) {
    Card(
        colors = (CardDefaults.cardColors(containerColor = Color.White)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            YearMonthBar(
                ymStr = calendarState.yearMonth.toYmString(),
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(6.dp))

            MonthlySummaryBar(
                total = calendarState.monthSummary.total,
                income = calendarState.monthSummary.income,
                expense = calendarState.monthSummary.expense
            )

            Spacer(modifier = Modifier.height(6.dp))

            WeekBar()

            Month(
                modifier = Modifier.weight(1f),
                calendarState = calendarState,
                onEvent = onEvent
            )
        }
    }
}

/* 년,월 출력 바 */
@Composable
private fun YearMonthBar(ymStr: String, onEvent: (CalendarEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(color = MainBlack),
            onClick = { onEvent(CalendarEvent.ClickedMovePrevMonth) }
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
            onClick = { onEvent(CalendarEvent.ClickedMoveNextMonth) }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "다음 월 이동 버튼",
                tint = Color.White
            )
        }
    }
}

/* 요일 출력 바 */
@Composable
private fun WeekBar() {
    val weeks = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Color.White)
            .border(width = 0.5.dp, color = DividerDefaults.color.copy(alpha = 0.8f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weeks.forEachIndexed { idx,                                                                                                                                                                                                                                 week ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = week,
                    fontSize = CalendarText,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (idx < weeks.lastIndex) {
                VerticalDivider(thickness = 0.5.dp, color = DividerDefaults.color.copy(alpha = 0.8f))
            }
        }
    }
}

/* 한 달 출력 */
@Composable
private fun Month(modifier: Modifier, calendarState: CalendarState, onEvent: (CalendarEvent) -> Unit) {
    val firstDayOfWeek = calendarState.yearMonth.atDay(1).dayOfWeek   // 해당 월에서 1일의 요일
    val offset = firstDayOfWeek.value % 7       // 해당 월에서 1일의 요일 인덱스화 (일=0, 월=1, ..., 토=6)
    val lastDate = calendarState.yearMonth.lengthOfMonth()    // 해당 월의 총 일수
    val weeks = ((offset + lastDate + 6) / 7)   // 주수
    var date = 1 - offset

    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 0.5.dp, color = DividerDefaults.color.copy(alpha = 0.8f))
    ) {
        repeat(weeks) { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(7) { col ->
                    if (date < 1 || date > lastDate) {
                        EmptyDayBlock()
                    } else {
                        val currentDate = calendarState.yearMonth.atDay(date)
                        val isToday = currentDate == LocalDate.now()

                        DayBlock(
                            date = currentDate,
                            isToday = isToday,
                            summary = calendarState.dailySummaries[currentDate],
                            onEvent = onEvent)
                    }
                    date++

                    if (col < 6) {
                        VerticalDivider(thickness = 0.5.dp, color = DividerDefaults.color.copy(alpha = 0.8f), modifier = Modifier.fillMaxHeight())
                    }
                }
            }

            if (week < weeks - 1) {
                HorizontalDivider(thickness = 0.5.dp, color = DividerDefaults.color.copy(alpha = 0.8f))
            }
        }
    }
}

/* 빈 블럭 */
@Composable
private fun RowScope.EmptyDayBlock() {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .background(color = EmptyDayBlockColor)
    )
}

/* 날짜 블럭 */
@Composable
private fun RowScope.DayBlock(date: LocalDate, isToday: Boolean, summary: AmountSummary?, onEvent: (CalendarEvent) -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .background(if (isToday) TodayBlockColor else Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(true)
            ) { onEvent(CalendarEvent.ClickedDayBlock(date = date)) },
        contentAlignment = Alignment.TopEnd
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "${date.dayOfMonth}",
                    fontSize = CaptionText,
                    color = if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY) MainRed else MainBlack
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 4.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "${if (summary == null || summary.income == 0.toLong()) "" else summary.income}",
                    fontSize = CalendarText,
                    color = MainBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = CalendarText * 1f,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Text(
                    text = "${if (summary == null || summary.expense == 0.toLong()) "" else summary.expense}",
                    fontSize = CalendarText,
                    color = MainRed,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = CalendarText * 1f,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

//                Text(
//                    text = "${summary?.total ?: 0}",
//                    fontSize = CalendarText,
//                    color = MainBlack,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis,
//                    lineHeight = CalendarText * 1f,
//                    modifier = Modifier.padding(vertical = 2.dp)
//                )
            }
        }
    }
}