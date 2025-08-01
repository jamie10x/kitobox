package com.jamie.kitobox.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jamie.kitobox.features.discover.DiscoverScreen
import com.jamie.kitobox.features.library.LibraryScreen
import com.jamie.kitobox.features.notes.AllNotesScreen
import com.jamie.kitobox.features.notes.NotesSummaryScreen
import com.jamie.kitobox.features.onboarding.OnboardingScreen
import com.jamie.kitobox.features.reader.ReaderScreen
import com.jamie.kitobox.features.settings.SettingsScreen

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screensWithBottomBar = setOf("library", "discover", "all_notes", "settings")

    Scaffold(
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                AppBottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("onboarding") {
                OnboardingScreen(
                    onOnboardingComplete = {
                        navController.navigate("library") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }
            composable("library") {
                LibraryScreen(
                    onBookClick = { bookId -> navController.navigate("reader/$bookId") }
                )
            }
            composable("discover") { DiscoverScreen() }
            composable("all_notes") {
                AllNotesScreen(
                    onBookClick = { bookId -> navController.navigate("notes/$bookId") }
                )
            }
            composable("settings") { SettingsScreen() }

            composable(
                "reader/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.IntType })
            ) {
                ReaderScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToNotes = { bookId -> navController.navigate("notes/$bookId") }
                )
            }
            composable(
                "notes/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.IntType })
            ) {
                NotesSummaryScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onAnnotationClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    val navItems = listOf("Library", "Discover", "Notes", "Settings")
    val icons = listOf(
        Icons.AutoMirrored.Outlined.MenuBook, Icons.Outlined.Search,
        Icons.AutoMirrored.Outlined.Notes, Icons.Outlined.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navItems.forEachIndexed { index, screen ->
            val route = when (screen) {
                "Notes" -> "all_notes"
                else -> screen.lowercase()
            }
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = screen) },
                label = { Text(screen) },
                selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}