package com.jamie.kitobox.features.settings

import androidx.lifecycle.ViewModel
import com.jamie.kitobox.data.repository.AppTheme
import com.jamie.kitobox.data.repository.FontStyle
import com.jamie.kitobox.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val currentTheme: StateFlow<AppTheme> = userPreferencesRepository.theme
    fun updateTheme(newTheme: AppTheme) {
        userPreferencesRepository.setTheme(newTheme)
    }

    val currentFontStyle: StateFlow<FontStyle> = userPreferencesRepository.fontStyle
    fun updateFontStyle(newStyle: FontStyle) {
        userPreferencesRepository.setFontStyle(newStyle)
    }

    val currentFontSize: StateFlow<Float> = userPreferencesRepository.fontSize
    fun updateFontSize(newSize: Float) {
        userPreferencesRepository.setFontSize(newSize)
    }
}