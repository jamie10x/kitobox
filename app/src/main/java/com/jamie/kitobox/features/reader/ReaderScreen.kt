package com.jamie.kitobox.features.reader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.compose.AndroidFragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.pdf.viewer.fragment.PdfViewerFragment
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel = koinViewModel(),
    onNavigateBack: () -> Unit, // <-- ADD
    onNavigateToNotes: (Int) -> Unit
) {
    val book by viewModel.book.collectAsStateWithLifecycle()
    val documentUri = remember(book) { book?.fileUri?.toUri() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(book?.title ?: "Loading...", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Library")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToNotes(viewModel.bookId) }) {
                        Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = "View Notes")
                    }
                }
            )
        }
    ){ innerPadding ->
        if (documentUri != null) {
            AndroidFragment(
                clazz = PdfViewerFragment::class.java,
                arguments = bundleOf("documentUri" to documentUri),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}