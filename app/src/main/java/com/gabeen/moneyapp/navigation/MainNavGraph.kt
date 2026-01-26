package com.gabeen.moneyapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabeen.moneyapp.ui.category.CategoryViewModel
import com.gabeen.moneyapp.ui.category.add.CategoryAddScreen
import com.gabeen.moneyapp.ui.category.detail.CategoryDetailScreen
import com.gabeen.moneyapp.ui.category.edit.CategoryEditScreen
import com.gabeen.moneyapp.ui.category.manage.CategoryManageScreen
import com.gabeen.moneyapp.ui.effect.CollectUiEffect
import com.gabeen.moneyapp.ui.history.HistoryViewModel
import com.gabeen.moneyapp.ui.history.add.HistoryAddScreen
import com.gabeen.moneyapp.ui.history.detail.HistoryDetailScreen
import com.gabeen.moneyapp.ui.history.edit.HistoryEditScreen
import com.gabeen.moneyapp.ui.home.HomeScreen
import com.gabeen.moneyapp.ui.home.calendar.CalendarViewModel
import com.gabeen.moneyapp.ui.home.statistic.StatisticViewModel

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val statisticViewModel: StatisticViewModel = hiltViewModel()

    CollectUiEffect(
        navController = navController,
        calendarViewModel.uiEffect,
        historyViewModel.uiEffect,
        categoryViewModel.uiEffect,
        statisticViewModel.uiEffect
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController, calendarViewModel, historyViewModel, statisticViewModel) }   // 홈 화면

        composable("historyAdd") { HistoryAddScreen(historyViewModel) }         // 내역 추가 화면
        composable("historyDetail") { HistoryDetailScreen(historyViewModel) }   // 내역 상세 화면
        composable("historyEdit") { HistoryEditScreen(historyViewModel) }       // 내역 수정 화면

        composable("categoryAdd") { CategoryAddScreen(categoryViewModel) }          // 카테고리 추가 화면
        composable("categoryDetail") { CategoryDetailScreen(categoryViewModel) }    // 카테고리 상세 화면
        composable("categoryEdit") { CategoryEditScreen(categoryViewModel) }        // 카테고리 수정 화면
        composable("categoryManage") { CategoryManageScreen(categoryViewModel) }    // 카테고리 관리 화면
    }
}