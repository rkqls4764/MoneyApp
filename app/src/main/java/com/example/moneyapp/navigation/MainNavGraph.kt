package com.example.moneyapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moneyapp.ui.effect.CollectUiEffect
import com.example.moneyapp.ui.history.add.HistoryAddScreen
import com.example.moneyapp.ui.home.HomeScreen
import com.example.moneyapp.ui.home.calendar.CalendarViewModel

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val calendarViewModel: CalendarViewModel = hiltViewModel()

    CollectUiEffect(
        navController = navController,
        calendarViewModel.uiEffect
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController, calendarViewModel) } // 홈 화면
        composable("historyAdd") { HistoryAddScreen(navController) }        // 내역 추가 화면
    }
}