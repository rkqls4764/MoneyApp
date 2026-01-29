package com.gabeen.moneyapp.ui.theme

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun MoneyAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun FixedFontScaleTheme(content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val fixedConfig = Configuration(configuration).apply {
        fontScale = 1f
    }

    val fixedDensity = Density(
        density = density.density,
        fontScale = 1f
    )

    CompositionLocalProvider(
        LocalConfiguration provides fixedConfig,
        LocalDensity provides fixedDensity
    ) {
        content()
    }
}