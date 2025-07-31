package com.jamie.kitobox.features.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.model.Book
import com.jamie.kitobox.data.repository.BookRepository // <-- IMPORT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReaderViewModel(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookId: Int = savedStateHandle.get<Int>("bookId") ?: -1

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    init {
        if (bookId != -1) {
            viewModelScope.launch {
                _book.value = bookRepository.getBookById(bookId)
            }
        }
    }
}