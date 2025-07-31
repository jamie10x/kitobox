package com.jamie.kitobox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jamie.kitobox.features.library.LibraryScreen
import com.jamie.kitobox.features.notes.NotesSummaryScreen
import com.jamie.kitobox.features.onboarding.OnboardingScreen
import com.jamie.kitobox.features.reader.ReaderScreen
import com.jamie.kitobox.features.settings.SettingsScreen

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("onboarding") {
            OnboardingScreen(
                onOnboardingComplete = {
                    // Navigate to library and clear onboarding from the back stack
                    navController.navigate("library") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("library") {
            LibraryScreen(
                onBookClick = { bookId ->
                    // Navigate with the book's ID
                    navController.navigate("reader/${bookId}")
                },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable(
            route = "reader/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType }) // Expect an Int
        ) {
            ReaderScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNotes = { bookId ->
                    navController.navigate("notes/${bookId}")
                }
            )
        }

        composable(
            route = "notes/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) {
            NotesSummaryScreen(
                onNavigateBack = { navController.popBackStack() },
                onAnnotationClick = { pageNum ->
                    // TODO: Later, this will jump to the specific page in the reader
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}