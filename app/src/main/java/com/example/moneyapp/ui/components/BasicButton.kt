package com.example.moneyapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.MainBlue
import com.example.moneyapp.ui.theme.MainYellow

/* 기본 플로팅 버튼 */
@Composable
fun BasicFloatingButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MainYellow,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "플로팅 버튼",
            modifier = Modifier.size(30.dp)
        )
    }
}

/* 기본 버튼 */
@Composable
fun BasicButton(
    name: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth().height(46.dp).padding(horizontal = 10.dp),
        onClick = { onClick() },
        shape = RoundedCornerShape(percent = 30),
        colors = ButtonDefaults.buttonColors(
            containerColor = MainYellow,
            contentColor = Color.White
        )
    ) {
        Text(
            text = name,
            fontSize = BodyText,
            fontWeight = FontWeight.Bold
        )
    }
}