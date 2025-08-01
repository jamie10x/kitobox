package com.jamie.kitobox.features.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.model.AnnotationType
import com.jamie.kitobox.data.model.Book
import com.jamie.kitobox.data.model.BookAnnotation
import com.jamie.kitobox.data.repository.AnnotationRepository
import com.jamie.kitobox.data.repository.BookRepository // <-- IMPORT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReaderViewModel(
    private val bookRepository: BookRepository,
    private val annotationRepository: AnnotationRepository,
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

    fun saveAnnotation(note: String, type: AnnotationType) {
        if (bookId == -1) return // Do not save if the bookId is invalid

        viewModelScope.launch {
            val newAnnotation = BookAnnotation(
                bookId = bookId,
                pageNumber = 1, // TODO: We need a way to get the current page number
                note = note,
                highlightData = if (type == AnnotationType.HIGHLIGHT) "..." else null,
                type = type
            )
            annotationRepository.saveAnnotation(newAnnotation)
        }
    }
}