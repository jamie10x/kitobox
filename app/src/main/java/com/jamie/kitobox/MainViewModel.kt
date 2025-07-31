package com.jamie.kitobox

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.jamie.kitobox.data.repository.AppTheme
import com.jamie.kitobox.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"

class MainViewModel(
    prefs: SharedPreferences,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _startDestination = MutableStateFlow("onboarding")
    val startDestination: StateFlow<String> = _startDestination

    val currentTheme: StateFlow<AppTheme> = userPreferencesRepository.theme

    init {
        if (prefs.getBoolean(ONBOARDING_COMPLETED_KEY, false)) {
            _startDestination.value = "library"
        }
    }
}