package com.jamie.kitobox.features.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Highlight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jamie.kitobox.data.model.AnnotationType
import com.jamie.kitobox.data.model.BookAnnotation
import com.jamie.kitobox.ui.theme.KitoboxTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotesSummaryScreen(
    viewModel: NotesSummaryViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onAnnotationClick: (Int) -> Unit
) {
    val annotations by viewModel.annotations.collectAsStateWithLifecycle()

    NotesSummaryContent(
        annotations = annotations,
        onNavigateBack = onNavigateBack,
        onAnnotationClick = onAnnotationClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesSummaryContent(
    annotations: List<BookAnnotation>,
    onNavigateBack: () -> Unit,
    onAnnotationClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Annotations", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Navigate back") } },
                actions = { IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Default.Search, "Search annotations") } }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (annotations.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(annotations) { annotation ->
                        AnnotationCard(annotation = annotation, onClick = { onAnnotationClick(annotation.pageNumber) })
                    }
                }
            }
        }
    }
}

@Composable
fun AnnotationCard(annotation: BookAnnotation, onClick: () -> Unit) {
    val icon = when (annotation.type) {
        AnnotationType.NOTE -> Icons.AutoMirrored.Filled.Notes
        AnnotationType.HIGHLIGHT -> Icons.Outlined.Highlight
    }

    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = annotation.type.name, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = annotation.note ?: "Highlighted text snippet here...", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Page ${annotation.pageNumber}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Text(text = "No notes or highlights for this book yet.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun NotesSummaryScreenEmptyPreview() {
    KitoboxTheme {
        NotesSummaryContent(
            annotations = emptyList(),
            onNavigateBack = {},
            onAnnotationClick = {}
        )
    }
}