package com.jamie.kitobox.features.annotation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jamie.kitobox.data.model.AnnotationType
import com.jamie.kitobox.ui.theme.KitoboxTheme

@Composable
fun AnnotationSheetContent(
    onSave: (note: String, type: AnnotationType) -> Unit,
    onCancel: () -> Unit
) {
    var noteText by remember { mutableStateOf("") }
    var annotationType by remember { mutableStateOf(AnnotationType.NOTE) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Toolbar for selecting annotation type ---
        AnnotationToolbar(
            selectedType = annotationType,
            onTypeSelected = { annotationType = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Text input for the note ---
        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("This is a preview of your note. Tap to edit.") },
            label = { Text("Your Note") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Color picker (placeholder UI) ---
        ColorPicker()

        Spacer(modifier = Modifier.height(24.dp))

        // --- Save and Cancel buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = { onSave(noteText, annotationType) },
                modifier = Modifier.weight(1f),
                enabled = noteText.isNotBlank() // Only enable save if there's text
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun AnnotationToolbar(selectedType: AnnotationType, onTypeSelected: (AnnotationType) -> Unit) {
    // In a real app, this could be a Toggleable-like component
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ToolbarButton(
            text = "Highlight",
            icon = Icons.Outlined.Highlight,
            isSelected = selectedType == AnnotationType.HIGHLIGHT,
            onClick = { onTypeSelected(AnnotationType.HIGHLIGHT) }
        )
        ToolbarButton(
            text = "Note",
            icon = Icons.Outlined.Notes,
            isSelected = selectedType == AnnotationType.NOTE,
            onClick = { onTypeSelected(AnnotationType.NOTE) }
        )
        // Future buttons like Pen or Erase could go here
    }
}

@Composable
fun ToolbarButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val colors = if (isSelected) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors()
    Button(onClick = onClick, colors = colors) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun ColorPicker() {
    val colors = listOf(Color.Yellow, Color.Red, Color.Green, Color.Blue)
    var selectedColor by remember { mutableStateOf(colors.first()) }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.5f))
                    .border(
                        width = 2.dp,
                        color = if (selectedColor == color) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { selectedColor = color }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnnotationSheetContentPreview() {
    KitoboxTheme {
        Surface {
            AnnotationSheetContent(onSave = { _, _ -> }, onCancel = {})
        }
    }
}