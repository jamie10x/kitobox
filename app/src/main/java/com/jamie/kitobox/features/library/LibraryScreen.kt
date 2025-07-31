package com.jamie.kitobox.features.library

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
import androidx.compose.material3.Surface
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
import com.jamie.kitobox.R
import com.jamie.kitobox.ui.theme.KitoboxTheme

// Data class and dummy data remain the same
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val coverResId: Int,
    val progress: Float
)

val dummyBooks = listOf(
    Book(1, "The Silent Observer", "Ava Sterling", R.drawable.ic_launcher_background, 0.6f),
    Book(2, "Echoes of the Past", "Ethan Blackwood", R.drawable.ic_launcher_background, 0.3f),
    Book(3, "The Last Lighthouse", "Clara Morgan", R.drawable.ic_launcher_background, 0.9f),
    Book(4, "Whispers of the Wind", "Liam Carter", R.drawable.ic_launcher_background, 0.0f),
    Book(5, "The Secret Garden", "Isabelle Reed", R.drawable.ic_launcher_background, 1.0f),
    Book(6, "Beyond the Horizon", "Noah Bennett", R.drawable.ic_launcher_background, 0.1f),
)

@Composable
fun LibraryScreen() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Library", "Discover", "Notes", "Settings")
    val icons = listOf(Icons.AutoMirrored.Outlined.MenuBook, Icons.Outlined.Search,
        Icons.AutoMirrored.Outlined.Notes, Icons.Outlined.Settings)

    Scaffold(
        topBar = { LibraryTopAppBar() },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Handle Import */ },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary, // Use theme color
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Import Book",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary // Ensure icon color contrasts
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
            BookGrid(books = dummyBooks)
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
        // Make TopAppBar transparent to use Scaffold's background color
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
fun BookGrid(books: List<Book>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books.size) { index ->
            BookCard(book = books[index])
        }
    }
}

@Composable
fun BookCard(book: Book) {
    Column(modifier = Modifier.clickable(onClick = { /* TODO: Handle book click */ })) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = painterResource(id = book.coverResId),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f),
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
            text = book.author,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Use theme color for subtitles
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


// You can use the Preview annotation to test both themes!
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LibraryScreenLightPreview() {
    KitoboxTheme(darkTheme = false) {
        Surface {
            LibraryScreen()
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LibraryScreenDarkPreview() {
    KitoboxTheme(darkTheme = true) {
        Surface {
            LibraryScreen()
        }
    }
}