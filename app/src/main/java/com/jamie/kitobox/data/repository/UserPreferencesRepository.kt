package com.jamie.kitobox.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.core.content.edit

enum class AppTheme {
    LIGHT, DARK, SEPIA
}

class UserPreferencesRepository(private val prefs: SharedPreferences) {

    private val _theme = MutableStateFlow(AppTheme.valueOf(prefs.getString(KEY_THEME, AppTheme.LIGHT.name) ?: AppTheme.LIGHT.name))
    val theme: StateFlow<AppTheme> = _theme

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        prefs.edit { putString(KEY_THEME, theme.name) }
    }

    companion object {
        private const val KEY_THEME = "app_theme"
    }
}