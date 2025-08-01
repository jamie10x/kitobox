package com.jamie.kitobox.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jamie.kitobox.data.repository.AppTheme
import com.jamie.kitobox.data.repository.FontStyle
import com.jamie.kitobox.ui.theme.KitoboxTheme
import org.koin.androidx.compose.koinViewModel

/**
 * Composable function for the Settings Screen.
 * This screen allows users to customize application settings like theme, font style, and font size.
 *
 * @param viewModel The [SettingsViewModel] used to manage and persist settings.
 *                  It is injected using Koin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    // Collect current preference states from the ViewModel.
    // `collectAsStateWithLifecycle` ensures that collection happens only when the Composable is active.
    val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle(initialValue = AppTheme.LIGHT)
    val currentFontStyle by viewModel.currentFontStyle.collectAsStateWithLifecycle(initialValue = FontStyle.SERIF)
    val currentFontSize by viewModel.currentFontSize.collectAsStateWithLifecycle(initialValue = 16f)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
            // "APPEARANCE" section header
            item { SettingsSection("APPEARANCE") }
            item {
                ThemeSetting(
                    title = "Theme",
                    subtitle = "Light, Dark, Sepia",
                    value = currentTheme,
                    onClick = {
                        // Cycle through available themes
                        val nextTheme = when (currentTheme) {
                            AppTheme.LIGHT -> AppTheme.DARK
                            AppTheme.DARK -> AppTheme.SEPIA
                            AppTheme.SEPIA -> AppTheme.LIGHT
                        }
                        viewModel.updateTheme(nextTheme)
                    }
                )
            }
            item {
                FontStyleSetting(
                    title = "Font Style",
                    subtitle = "Serif, Sans, Dyslexia-friendly",
                    value = currentFontStyle,
                    onSelection = { newStyle -> viewModel.updateFontStyle(newStyle) }
                )
            }
            item {
                FontSizeSetting(
                    title = "Default Font Size",
                    value = currentFontSize,
                    onValueChange = { newSize -> viewModel.updateFontSize(newSize) }
                )
            }
        }
    }


/**
 * Composable for displaying a theme setting item.
 *
 * @param title The title of the setting (e.g., "Theme").
 * @param subtitle A descriptive subtitle for the setting (e.g., "Light, Dark, Sepia").
 * @param value The current [AppTheme] value.
 * @param onClick Lambda function to be invoked when the setting item is clicked.
 */
@Composable
fun ThemeSetting(title: String, subtitle: String, value: AppTheme, onClick: () -> Unit) {
    SettingItem(
        title = title,
        subtitle = subtitle,
        valueText = value.name.lowercase().replaceFirstChar { it.titlecase() },
        onClick = onClick
    )
}

/**
 * Composable for displaying and selecting a font style.
 * This uses an [ExposedDropdownMenuBox] to present font style options.
 *
 * @param title The title of the setting (e.g., "Font Style").
 * @param subtitle A descriptive subtitle (e.g., "Serif, Sans, Dyslexia-friendly").
 * @param value The current [FontStyle] value.
 * @param onSelection Lambda function invoked when a new font style is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontStyleSetting(
    title: String,
    subtitle: String,
    value: FontStyle,
    onSelection: (FontStyle) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // State to control dropdown visibility

    // `ExposedDropdownMenuBox` provides the framework for a dropdown menu.
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        // The `SettingItem` acts as the anchor for the dropdown.
        // `menuAnchor` modifier specifies this relationship.
        SettingItem(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
            title = title,
            subtitle = subtitle,
            valueText = value.name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() },
            onClick = { expanded = true } // Clicking the item opens the dropdown
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            FontStyle.entries.forEach { style ->
                DropdownMenuItem(
                    text = { Text(style.name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() }) },
                    onClick = {
                        onSelection(style)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Composable for adjusting the font size using a [Slider].
 *
 * @param title The title of the setting (e.g., "Default Font Size").
 * @param value The current font size value (as a Float).
 * @param onValueChange Lambda function invoked when the slider value changes.
 *                      It provides the new font size.
 */
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
            steps = 5 // Defines discrete steps for the slider (e.g., 12, 14, 16, 18, 20, 22, 24)
        )
    }
}


/**
 * A generic composable for displaying a section header in the settings screen.
 *
 * @param title The text to display as the section title.
 */
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

/**
 * A generic, reusable composable for an individual setting item.
 * It typically displays a title, subtitle, current value, and an icon.
 *
 * @param modifier Optional [Modifier] for customizing the layout.
 * @param title The main title of the setting.
 * @param subtitle A descriptive subtitle for the setting.
 * @param valueText The text representation of the current setting value.
 * @param onClick Lambda function to be executed when the item is clicked.
 */
@Composable
fun SettingItem(modifier: Modifier = Modifier,title: String, subtitle: String, valueText: String, onClick: () -> Unit) {
    Row(
        modifier = modifier
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
            Text(text = valueText, color = MaterialTheme.colorScheme.primary)
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


/**
 * Preview function for the [SettingsScreen].
 * This allows for a visual preview of the settings screen layout in Android Studio.
 * Note: This preview is not interactive and uses default values.
 */
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    KitoboxTheme {
        SettingsScreen()
    }
}