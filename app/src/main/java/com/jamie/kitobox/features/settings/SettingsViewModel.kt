package com.jamie.kitobox.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.repository.AppTheme
import com.jamie.kitobox.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val currentTheme: StateFlow<AppTheme> = userPreferencesRepository.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.LIGHT
        )

    fun updateTheme(newTheme: AppTheme) {
        userPreferencesRepository.setTheme(newTheme)
    }
}