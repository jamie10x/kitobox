package com.jamie.kitobox.features.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.model.Book
import com.jamie.kitobox.data.repository.BookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val books: StateFlow<List<Book>> = bookRepository.getAllBooks()
        .combine(_searchQuery) { allBooks, query ->
            if (query.isBlank()) {
                allBooks
            } else {
                allBooks.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            (it.author?.contains(query, ignoreCase = true) == true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun importBook(uri: Uri) {
        viewModelScope.launch {
            val newBook = Book(
                title = uri.lastPathSegment ?: "Unknown Title",
                author = null,
                fileUri = uri.toString(),
                coverImagePath = null
            )
            bookRepository.insertBook(newBook)
        }
    }
}