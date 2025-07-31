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
    primary = SepiaPrimary,
    background = SepiaBackground,
    surface = SepiaBackground,
    onPrimary = Color.White,
    onBackground = SepiaOnSurface,
    onSurface = SepiaOnSurface,
    onSurfaceVariant = SepiaOnSurface.copy(alpha = 0.7f)
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