package com.jamie.kitobox.features.library

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.model.Book
import com.jamie.kitobox.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LibraryViewModel (
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    init {
        viewModelScope.launch {
            bookRepository.getAllBooks().collectLatest { bookList ->
                _books.value = bookList
            }
        }
    }

    fun importBook(uri: Uri) {
        viewModelScope.launch {
            val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            val fileType = if (extension.equals("epub", ignoreCase = true)) "epub" else "pdf"

            val newBook = Book(
                title = uri.lastPathSegment ?: "Unknown Title",
                author = null,
                fileUri = uri.toString(),
                fileType = fileType,
                coverImagePath = null
            )
            bookRepository.insertBook(newBook)
        }
    }
}