/*
 * MIT License
 *
 * Copyright (c) 2023 Developer Chunk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 */

package com.example.moneyapp.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.ui.theme.BodyText
import com.example.moneyapp.ui.theme.CaptionText

@Composable
fun PieChart(
    data: List<Float>,
    colors: List<Color>,
    radiusOuter: Dp = 140.dp,
    chartBarWidth: Dp = 35.dp,
    animDuration: Int = 1000,
) {
    val totalSum = data.sum()
    val floatValue = mutableListOf<Float>()

    data.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    var animationPlayed by remember { mutableStateOf(false) }

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            val padding = chartBarWidth / 2

            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f + chartBarWidth)
                    .rotate(animateRotation)
                    .padding(padding)
            ) {
                var lastValue = 0f

                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = lastValue,
                        sweepAngle = value,
                        useCenter = false,
                        style = Stroke(width = chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }
    }
}

@Composable
fun DetailsPieChart(
    data: Map<Category, Float>,
    colors: List<Color>,
    onClick: (Category) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        data.entries.forEachIndexed { index, entry ->
            DetailsPieChartItem(
                data = Pair(entry.key.name, entry.value),
                color = colors[index % colors.size],
                onClick = { onClick(entry.key) }
            )
        }
    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Float>,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(46.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.first,
                    fontWeight = FontWeight.Medium,
                    fontSize = BodyText,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "${data.second}%",
                    fontWeight = FontWeight.Medium,
                    fontSize = BodyText,
                    color = Color.Gray
                )
            }
        }
    }
}