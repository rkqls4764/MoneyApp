package com.example.moneyapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.moneyapp.ui.components.BasicBottomBar
import com.example.moneyapp.ui.components.BasicFloatingButton
import com.example.moneyapp.ui.components.BasicTopBar
import com.example.moneyapp.ui.history.HistoryViewModel
import com.example.moneyapp.ui.home.calendar.CalendarScreen
import com.example.moneyapp.ui.home.calendar.CalendarViewModel
import com.example.moneyapp.ui.home.setting.SettingScreen
import com.example.moneyapp.ui.home.statistic.StatisticScreen
import com.example.moneyapp.ui.home.statistic.StatisticViewModel
import com.example.moneyapp.ui.theme.MainBlack

/* 홈 화면 */
@Composable
fun HomeScreen(navController: NavController, calendarViewModel: CalendarViewModel, historyViewModel: HistoryViewModel, statisticViewModel: StatisticViewModel) {
    var showScreenNum by rememberSaveable { mutableStateOf(1) }  // 출력 화면
    var isFilterClicked by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BasicTopBar(
                title = when (showScreenNum) {
                    0 -> { "통계" }
                    1 -> { "캘린더" }
                    2 -> { "설정" }
                    else -> ""
                },
                showNavIcon = false,
                actIcon = if (showScreenNum == 0) Icons.Default.FilterAlt else null,
                onClickActIcon = {
                    if (showScreenNum == 0) isFilterClicked = true
                }
            )
        },
        bottomBar = {
            BasicBottomBar(
                selected = showScreenNum,
                onSelected = { showScreenNum = it }
            )
        },
        floatingActionButton = {
            // 캘린더 화면에만 내역 추가 플로팅 버튼 출력
            if (showScreenNum == 1) {
                BasicFloatingButton(
                    onClick = { navController.navigate("historyAdd") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            when (showScreenNum) {
                0 -> { StatisticScreen(statisticViewModel = statisticViewModel, historyViewModel = historyViewModel, isFilterClicked = isFilterClicked, onDismiss = { isFilterClicked = false }) }
                1 -> { CalendarScreen(calendarViewModel = calendarViewModel, historyViewModel = historyViewModel) }
                2 -> { SettingScreen(navController = navController) }
            }
        }
    }
}