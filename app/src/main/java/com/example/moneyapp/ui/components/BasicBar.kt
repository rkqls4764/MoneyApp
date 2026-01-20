package com.example.moneyapp.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SsidChart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.moneyapp.ui.theme.MainBlack
import com.example.moneyapp.ui.theme.MainRed
import com.example.moneyapp.ui.theme.TitleText

/* 기본 상단바 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(
    title: String,                      // 제목
    showNavIcon: Boolean = true,        // 뒤로 가기 아이콘 여부
    actIcon: ImageVector? = null,       // 액션 아이콘
    actTint: Color = Color.Black,       // 액션 아이콘 색상
    onClickNavIcon: () -> Unit = {},    // 뒤로 가기 아이콘 클릭 이벤트
    onClickActIcon: () -> Unit = {}     // 액션 아이콘 클릭 이벤트
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = TitleText,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (showNavIcon) {
                IconButton(
                    onClick = {
                        onClickNavIcon()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로 가기 버튼"
                    )
                }
            }
        },
        actions = {
            if (actIcon != null) {
                IconButton(
                    onClick = {
                        onClickActIcon()
                    }
                ) {
                    Icon(
                        imageVector = actIcon,
                        contentDescription = "액션 버튼",
                        tint = actTint
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

/* 삭제 아이콘 상단바 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteIconTopBar(
    title: String,                      // 제목
    onClickNavIcon: () -> Unit = {},    // 뒤로 가기 아이콘 클릭 이벤트
    onClickActIcon: () -> Unit = {}     // 삭제 아이콘 클릭 이벤트
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = TitleText,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onClickNavIcon()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로 가기 버튼"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onClickActIcon()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = "삭제 버튼",
                    tint = MainRed
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

/* 기본 하단 바 */
@Composable
fun BasicBottomBar(
    selected: Int,
    onSelected: (Int) -> Unit
) {
    val itemColors = NavigationBarItemDefaults.colors(
        indicatorColor = MainBlack,     // 선택된 버튼 배경
        selectedIconColor = Color.White // 선택 아이콘 색
    )

    NavigationBar(
        windowInsets = WindowInsets(0),
        containerColor = Color.White
    ) {
        NavigationBarItem(
            selected = selected == 0,
            onClick = { onSelected(0) },
            icon = { Icon(imageVector = Icons.Outlined.SsidChart, contentDescription = "통계 아이콘")},
            label = { Text("통계") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selected == 1,
            onClick = { onSelected(1) },
            icon = { Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = "캘린더 아이콘")},
            label = { Text("캘린더") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = selected == 2,
            onClick = { onSelected(2) },
            icon = { Icon(imageVector = Icons.Outlined.Settings, contentDescription = "설정 아이콘")},
            label = { Text("설정") },
            colors = itemColors
        )
    }
}