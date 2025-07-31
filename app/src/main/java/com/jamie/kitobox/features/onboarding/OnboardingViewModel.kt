package com.jamie.kitobox.features.onboarding

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.jamie.kitobox.ONBOARDING_COMPLETED_KEY
import androidx.core.content.edit


class OnboardingViewModel(
    private val prefs: SharedPreferences,
) : ViewModel() {
    fun onOnboardingFinished() {
        prefs.edit { putBoolean(ONBOARDING_COMPLETED_KEY, true) }
    }
}