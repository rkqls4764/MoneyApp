package com.example.moneyapp.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moneyapp.ui.theme.CaptionText

/* 기본 드롭다운 필드 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDropDownField(
    modifier: Modifier = Modifier,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }  // 열림 여부

    val rotation by animateFloatAsState(
        targetValue = if (expanded) -180f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "dropdownRotation"
    )

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
            }
        }
    ) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth().menuAnchor(),
            value = selected,
            onValueChange = {},
            singleLine = true,
            readOnly = true,
            shape = RoundedCornerShape(percent = 50),
            colors = BasicOutlinedTextFieldColors(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            },
            enabled = enabled
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize().heightIn(max = 260.dp),
            containerColor = MaterialTheme.colorScheme.background
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            fontSize = CaptionText
                        )
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                    modifier = Modifier.background(color = Color.White)
                )
            }
        }
    }
}