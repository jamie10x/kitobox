package com.jamie.kitobox.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jamie.kitobox.data.repository.AppTheme

private val KitoboxDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.White,
    onBackground = OnDarkSurface,
    onSurface = OnDarkSurface,
    onSurfaceVariant = OnDarkSurfaceVariant,
    secondaryContainer = DarkSurface
)

private val KitoboxLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    /* Other default colors can be overridden here */
)

private val KitoboxSepiaColorScheme = lightColorScheme(
    primary = Color(0xFF8D6E63),
    background = Color(0xFFFBF0E3),
    surface = Color(0xFFFBF0E3),
    onPrimary = Color.White,
    onBackground = Color(0xFF5B4B3A),
    onSurface = Color(0xFF5B4B3A),
    onSurfaceVariant = Color(0xFF5B4B3A).copy(alpha = 0.7f),
    primaryContainer = Color(0xFF6D4C41),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEFEBE9)
)

@Composable
fun KitoboxTheme(
    appTheme: AppTheme = AppTheme.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.LIGHT -> KitoboxLightColorScheme
        AppTheme.DARK -> KitoboxDarkColorScheme
        AppTheme.SEPIA -> KitoboxSepiaColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}