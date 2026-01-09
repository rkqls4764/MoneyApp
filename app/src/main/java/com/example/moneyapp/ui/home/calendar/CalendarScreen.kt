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
import androidx.compose.ui.unit.dp
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CalendarText
import com.example.moneyapp.ui.theme.CaptionText
import com.example.moneyapp.ui.theme.EmptyDayBlockColor
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.ui.theme.MainBlue
import com.example.moneyapp.ui.theme.MainRed
import com.example.moneyapp.ui.theme.TitleText
import com.example.moneyapp.ui.theme.TodayBlockColor
import com.example.moneyapp.util.isEmptyList
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/* 캘린더 화면 */
@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel) {
    val onEvent = calendarViewModel::onEvent
    val calendarState by calendarViewModel.calendarState.collectAsState()

    if (calendarState.openSheet) {
        HistoriesBottomSheet(
            histories = listOf("d", "sdfsdfggsgsd", "sdfsdfggsgsd", "sdfsdfggsgsd","sdfsdfggsgsd" ,"sdfsdfggsgsd" ,"sdfsdfggsgsd","sdfsdfggsgsd","sdfsdfggsgsd", "d", "sdfsdfggsgsd", "sdfsdfggsgsd", "sdfsdfggsgsd","sdfsdfggsgsd" ,"sdfsdfggsgsd" ,"sdfsdfggsgsd"),
            date = calendarState.selectedDate,
            onEvent = onEvent
        )
    }

    Column(
        modifier = Modifier.padding(bottom = 80.dp)
    ) {
        Calendar(
            yearMonth = calendarState.yearMonth,
            onEvent = onEvent
        )
    }
}

/* 내역 목록 바텀 시트 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoriesBottomSheet(histories: List<String>, date: LocalDate, onEvent: (CalendarEvent) -> Unit) {
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

            DailySummaryBar(
                total = Int.MAX_VALUE,
                income = 65000,
                expense = 15000
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    isEmptyList(
                        list = histories,
                        text = "등록된 내역이 없습니다"
                    )
                }

                items(histories) { history ->

                    HistoryItem(history, onEvent)
                }
            }
        }
    }
}

/* 내역 목록 아이템 */
@Composable
private fun HistoryItem(history: String, onEvent: (CalendarEvent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = { /* TODO: 내역 목록 아이템 클릭 이벤트 - 내역 상세 조회 화면으로 이동 */ }),
        border = BorderStroke(width = 0.5.dp, color = Color.LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(vertical = 14.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "제목",
                color = MainBlack,
                fontSize = BodyText
            )

            Text(
                text = "금액",
                color = MainBlue,
                fontSize = BodyText,
                fontWeight = FontWeight.SemiBold
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
private fun DailySummaryBar(total: Int, income: Int, expense: Int) {
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
private fun MonthlySummaryBar(total: Int, income: Int, expense: Int) {
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
private fun RowScope.SummaryItem(name: String, price: Int, color: Color) {
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
                text = String.format("%,d", price),
                fontSize = CaptionText,
                color = color,
                maxLines = 1,
                modifier = Modifier.horizontalScroll(state = rememberScrollState()) // 길이가 길면 좌우 스크롤
            )
        }
    }
}

/* 캘린더 */
@Composable
private fun Calendar(yearMonth: YearMonth, onEvent: (CalendarEvent) -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
    val ymStr = remember(yearMonth) { yearMonth.format(formatter) }

    Card(
        colors = (CardDefaults.cardColors(containerColor = Color.White)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            YearMonthBar(
                ymStr = ymStr,
                onEvent = onEvent)

            Spacer(modifier = Modifier.height(6.dp))

            MonthlySummaryBar(
                total = Int.MAX_VALUE,
                income = 65000,
                expense = 15000
            )

            Spacer(modifier = Modifier.height(6.dp))

            WeekBar()

            Month(
                modifier = Modifier.weight(1f),
                yearMonth = yearMonth,
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
private fun Month(modifier: Modifier, yearMonth: YearMonth, onEvent: (CalendarEvent) -> Unit) {
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek   // 해당 월에서 1일의 요일
    val offset = firstDayOfWeek.value % 7       // 해당 월에서 1일의 요일 인덱스화 (일=0, 월=1, ..., 토=6)
    val lastDate = yearMonth.lengthOfMonth()    // 해당 월의 총 일수
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
                        val currentDate = yearMonth.atDay(date)
                        val isToday = currentDate == LocalDate.now()

                        DayBlock(currentDate, isToday, onEvent)
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
private fun RowScope.DayBlock(date: LocalDate, isToday: Boolean, onEvent: (CalendarEvent) -> Unit) {
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp, end = 3.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "${date.dayOfMonth}",
                    fontSize = CaptionText,
                    color = if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY) MainRed else MainBlack
                )
            }
        }
    }
}