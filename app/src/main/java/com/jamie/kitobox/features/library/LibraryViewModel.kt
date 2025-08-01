package com.jamie.kitobox.features.library

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.model.Book
import com.jamie.kitobox.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(
    application: Application,
    private val bookRepository: BookRepository
) : AndroidViewModel(application) {

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
            val contentResolver = getApplication<Application>().contentResolver
            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, takeFlags)

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