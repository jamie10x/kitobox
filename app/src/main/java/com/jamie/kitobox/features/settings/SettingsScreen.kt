package com.jamie.kitobox.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jamie.kitobox.data.repository.AppTheme
import com.jamie.kitobox.ui.theme.KitoboxTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                SettingsSection("APPEARANCE")
            }
            item {
                // For now, this is a placeholder. We will make it functional later.
                SettingItem(
                    title = "Theme",
                    subtitle = "Light, Dark, Sepia",
                    // Display the current theme's name
                    value = currentTheme.name.lowercase().replaceFirstChar { it.titlecase() }
                ) {
                    // In a real scenario, this would open a dialog to select the theme.
                    // For now, we'll just cycle through them for demonstration.
                    val nextTheme = when (currentTheme) {
                        AppTheme.LIGHT -> AppTheme.DARK
                        AppTheme.DARK -> AppTheme.SEPIA
                        AppTheme.SEPIA -> AppTheme.LIGHT
                    }
                    viewModel.updateTheme(nextTheme)
                }
            }
            item {
                SettingItem(
                    title = "Font Style",
                    subtitle = "Serif, Sans, Dyslexia-friendly",
                    value = "Serif" // Placeholder
                ) { /* TODO */ }
            }
            item {
                FontSizeSetting(
                    title = "Default Font Size",
                    value = 16f, // Placeholder
                    onValueChange = {} // Placeholder
                )
            }
            item {
                SwitchSetting(
                    title = "Auto-Night Mode",
                    checked = true, // Placeholder
                    onCheckedChange = {} // Placeholder
                )
            }

            item {
                SettingsSection("DATA")
            }
            item {
                SwitchSetting(
                    title = "Backup / Export Notes",
                    checked = false, // Placeholder
                    onCheckedChange = {} // Placeholder
                )
            }
        }
    }
}

// --- Reusable Component Composables ---

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingItem(
    title: String,
    subtitle: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, color = MaterialTheme.colorScheme.primary)
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FontSizeSetting(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = "${value.toInt()}pt", color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 12f..24f,
            steps = 5
        )
    }
}

@Composable
fun SwitchSetting(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    KitoboxTheme {
        SettingsScreen(onNavigateBack = {})
    }
}