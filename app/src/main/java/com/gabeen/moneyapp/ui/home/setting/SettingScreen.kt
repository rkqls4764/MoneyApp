package com.gabeen.moneyapp.ui.home.setting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gabeen.moneyapp.ui.components.LoadingOverlay
import com.gabeen.moneyapp.ui.theme.BodyText
import com.gabeen.moneyapp.ui.theme.MainBlack

/* 설정 화면 */
@Composable
fun SettingScreen(navController: NavController, settingViewModel: SettingViewModel) {
    val settingState by settingViewModel.settingState.collectAsState()

    val context = LocalContext.current

    // SAF 파일 선택 런처 (csv)
    val csvPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()   // Storage Access Framework
    ) { uri ->
        if (uri != null) {
            settingViewModel.importCsv(context, uri)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingMenuButton(
                    icon = Icons.Default.Category,
                    text = "카테고리 관리",
                    onClick = { navController.navigate("categoryManage") }
                )

                SettingMenuButton(
                    icon = Icons.Default.Upload,
                    text = "파일 업로드",
                    onClick = {
                        csvPickerLauncher.launch(
                            arrayOf(
                                "text/csv",
                                "text/comma-separated-values",
                                "application/vnd.ms-excel"
                            )
                        )
                    }
                )
            }
        }

        LoadingOverlay(
            visible = settingState.isLoading
        )
    }
}

/* 설정 메뉴 버튼 */
@Composable
private fun SettingMenuButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(140.dp).height(140.dp),
        shape = RoundedCornerShape(percent = 20),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(width = 1.dp, color = MainBlack.copy(0.8f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "아이콘",
                tint = MainBlack.copy(alpha = 0.8f),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = text,
                fontSize = BodyText,
                fontWeight = FontWeight.SemiBold,
                color = MainBlack
            )
        }
    }
}