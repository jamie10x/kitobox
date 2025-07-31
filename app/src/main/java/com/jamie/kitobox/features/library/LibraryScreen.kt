package com.jamie.kitobox.features.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.LocalMall
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jamie.kitobox.R
import com.jamie.kitobox.data.model.Book
import com.jamie.kitobox.ui.theme.KitoboxTheme
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    onBookClick: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val books by viewModel.books.collectAsStateWithLifecycle()

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Library", "Discover", "Notes", "Settings")
    val icons = listOf(Icons.AutoMirrored.Outlined.MenuBook, Icons.Outlined.Search, Icons.AutoMirrored.Outlined.Notes, Icons.Outlined.Settings)

    // --- CHANGE 2: The file picker now calls the ViewModel to import the book ---
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.importBook(it) // Tell the ViewModel to save the book
            }
        }
    )

    Scaffold(
        topBar = { LibraryTopAppBar() },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = item == "Library",
                        onClick = {
                            if (item == "Settings") {
                                onNavigateToSettings()
                            }
                            // Later, we can add navigation for Discover and Notes
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { filePickerLauncher.launch(arrayOf("application/pdf")) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Import Book",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(24.dp))
            YourBooksHeader()
            Spacer(modifier = Modifier.height(16.dp))
            BookGrid(books = books, onBookClick = onBookClick)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopAppBar() {
    TopAppBar(
        title = {
            Text(
                "Library",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: Handle search icon click */ }) {
                Icon(Icons.Outlined.Search, contentDescription = "Search")
            }
            IconButton(onClick = { /* TODO: Handle store icon click */ }) {
                Icon(Icons.Outlined.LocalMall, contentDescription = "Store")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search in your library") },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search Icon") },
        shape = RoundedCornerShape(12.dp),
        // Let the theme handle the colors for both light and dark mode
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            // Use theme colors for consistency
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        )
    )
}

@Composable
fun YourBooksHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Your Books", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Row {
            IconButton(onClick = { /* TODO: Handle grid toggle */ }) {
                Icon(Icons.Outlined.GridView, contentDescription = "Grid View")
            }
            IconButton(onClick = { /* TODO: Handle sort */ }) {
                Icon(Icons.Outlined.SwapVert, contentDescription = "Sort Books")
            }
        }
    }
}

@Composable
fun BookGrid(books: List<Book>, onBookClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = books, key = { it.id }) { book ->
            BookCard(
                book = book,
                onBookClick = {
                    // When a book card is clicked, encode its URI and navigate
                    val encodedUri = URLEncoder.encode(book.fileUri, StandardCharsets.UTF_8.toString())
                    onBookClick(book.id)
                }
            )
        }
    }
}

// --- CHANGE 5: The BookCard now uses the real Book model ---
@Composable
fun BookCard(book: Book, onBookClick: () -> Unit) {
    Column(modifier = Modifier.clickable(onClick = onBookClick)) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            // TODO: Replace with a real cover image later
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = book.title,
                modifier = Modifier.fillMaxWidth().aspectRatio(3f / 4f),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { book.progress },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.author ?: "Unknown Author", // Handle potentially null author
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    KitoboxTheme {
        LibraryScreen(onBookClick = {}, onNavigateToSettings = {})
    }
}