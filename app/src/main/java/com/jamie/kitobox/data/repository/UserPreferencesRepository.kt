package com.jamie.kitobox.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

// Define enums for our settings for type-safety
enum class AppTheme { LIGHT, DARK, SEPIA }
enum class FontStyle { SERIF, SANS, DYSLEXIA_FRIENDLY }

class UserPreferencesRepository(private val prefs: SharedPreferences) {

    // --- Theme Preference ---
    private val _theme = MutableStateFlow(
        AppTheme.valueOf(prefs.getString(KEY_THEME, AppTheme.LIGHT.name)!!)
    )
    val theme: StateFlow<AppTheme> = _theme.asStateFlow()

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        prefs.edit().putString(KEY_THEME, theme.name).apply()
    }

    // --- Font Style Preference ---
    private val _fontStyle = MutableStateFlow(
        FontStyle.valueOf(prefs.getString(KEY_FONT_STYLE, FontStyle.SERIF.name)!!)
    )
    val fontStyle: StateFlow<FontStyle> = _fontStyle.asStateFlow()

    fun setFontStyle(style: FontStyle) {
        _fontStyle.value = style
        prefs.edit { putString(KEY_FONT_STYLE, style.name) }
    }

    // --- Font Size Preference ---
    private val _fontSize = MutableStateFlow(
        prefs.getFloat(KEY_FONT_SIZE, 16f)
    )
    val fontSize: StateFlow<Float> = _fontSize.asStateFlow()

    fun setFontSize(size: Float) {
        _fontSize.value = size
        prefs.edit().putFloat(KEY_FONT_SIZE, size).apply()
    }


    // --- Keys for SharedPreferences ---
    companion object {
        private const val KEY_THEME = "app_theme"
        private const val KEY_FONT_STYLE = "font_style"
        private const val KEY_FONT_SIZE = "font_size"
    }
}