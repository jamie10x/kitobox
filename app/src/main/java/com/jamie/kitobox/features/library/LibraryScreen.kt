package com.jamie.kitobox.features.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    onBookClick: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    // --- State is collected from the ViewModel ---
    val books by viewModel.books.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var selectedItem by remember { mutableIntStateOf(0) }
    val navItems = listOf("Library", "Discover", "Notes", "Settings")
    val icons = listOf(Icons.AutoMirrored.Outlined.MenuBook, Icons.Outlined.Search, Icons.AutoMirrored.Outlined.Notes, Icons.Outlined.Settings)

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri -> uri?.let { viewModel.importBook(it) } }
    )

    Scaffold(
        topBar = { LibraryTopAppBar() },
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = item == "Library",
                        onClick = { if (item == "Settings") onNavigateToSettings() }
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

            // --- Pass state down and receive events up ---
            SearchBar(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) }
            )

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
        title = { Text("Library", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
        actions = {
            IconButton(onClick = { /* TODO */ }) { Icon(Icons.Outlined.Search, "Search") }
            IconButton(onClick = { /* TODO */ }) { Icon(Icons.Outlined.LocalMall, "Store") }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

// --- UPDATED: SearchBar is now a stateless composable ---
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search in your library") },
        leadingIcon = { Icon(Icons.Outlined.Search, "Search Icon") },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
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
            IconButton(onClick = { /* TODO */ }) { Icon(Icons.Outlined.GridView, "Grid View") }
            IconButton(onClick = { /* TODO */ }) { Icon(Icons.Outlined.SwapVert, "Sort Books") }
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
                onBookClick = { onBookClick(book.id) }
            )
        }
    }
}

@Composable
fun BookCard(book: Book, onBookClick: () -> Unit) {
    Column(modifier = Modifier.clickable(onClick = onBookClick)) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
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
        Text(text = book.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = book.author ?: "Unknown Author", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    KitoboxTheme {
        // You'll see a warning here because the preview can't create a real ViewModel.
        // This is expected and can be ignored, or you can create a fake ViewModel for previews.
        LibraryScreen(onBookClick = {}, onNavigateToSettings = {})
    }
}