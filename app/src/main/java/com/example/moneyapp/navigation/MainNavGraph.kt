package com.example.moneyapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moneyapp.ui.category.CategoryViewModel
import com.example.moneyapp.ui.category.manage.CategoryManageScreen
import com.example.moneyapp.ui.effect.CollectUiEffect
import com.example.moneyapp.ui.history.HistoryViewModel
import com.example.moneyapp.ui.history.add.HistoryAddScreen
import com.example.moneyapp.ui.history.detail.HistoryDetailScreen
import com.example.moneyapp.ui.history.edit.HistoryEditScreen
import com.example.moneyapp.ui.home.HomeScreen
import com.example.moneyapp.ui.home.calendar.CalendarViewModel

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()

    CollectUiEffect(
        navController = navController,
        calendarViewModel.uiEffect,
        historyViewModel.uiEffect,
        categoryViewModel.uiEffect
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController, calendarViewModel, historyViewModel) }   // 홈 화면
        composable("historyAdd") { HistoryAddScreen(historyViewModel) }                         // 내역 추가 화면
        composable("historyDetail") { HistoryDetailScreen(historyViewModel) }                   // 내역 상세 화면
        composable("historyEdit") { HistoryEditScreen(historyViewModel) }                       // 내역 수정 화면
        composable("categoryManage") { CategoryManageScreen(categoryViewModel) }                // 카테고리 관리 화면
    }
}