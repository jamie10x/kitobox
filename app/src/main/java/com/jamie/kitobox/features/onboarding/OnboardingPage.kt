package com.jamie.kitobox.features.onboarding

import androidx.annotation.DrawableRes
import com.jamie.kitobox.R

data class OnboardingPage(
    val title: String,
    val description: String,
    @param:DrawableRes val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to Kitobox",
        description = "Your distraction-free reading sanctuary.",
        imageRes = R.drawable.ic_launcher_foreground // Placeholder image
    ),
    OnboardingPage(
        title = "Enjoy a clutter-free reading experience",
        description = "Tap anywhere to reveal the toolbar.",
        imageRes = R.drawable.ic_launcher_foreground // Placeholder image
    ),
    OnboardingPage(
        title = "Import your books",
        description = "Easily import PDFs and eBooks from your device or favorite cloud services.",
        imageRes = R.drawable.ic_launcher_foreground // Placeholder image
    ),
    OnboardingPage(
        title = "Highlight, annotate, and save your thoughts",
        description = "Keep track of important moments and ideas.",
        imageRes = R.drawable.ic_launcher_foreground // Placeholder image
    ),
    OnboardingPage(
        title = "Personalize Your Reading",
        description = "Choose your preferred theme and font to create the perfect reading environment.",
        imageRes = R.drawable.ic_launcher_foreground // Placeholder image
    )
)