package com.jamie.kitobox.features.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.model.Book
import com.jamie.kitobox.data.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AllNotesViewModel(
    bookRepository: BookRepository
) : ViewModel() {
    val booksWithAnnotations: StateFlow<List<Book>> = bookRepository.getBooksWithAnnotations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}