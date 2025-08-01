package com.jamie.kitobox.features.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.kitobox.data.model.BookAnnotation
import com.jamie.kitobox.data.repository.AnnotationRepository
import kotlinx.coroutines.flow.*

class NotesSummaryViewModel(
    annotationRepository: AnnotationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: Int = savedStateHandle.get<Int>("bookId") ?: -1

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val annotations: StateFlow<List<BookAnnotation>> =
        annotationRepository.getAnnotationsForBook(bookId)
            .combine(_searchQuery) { allAnnotations, query ->
                if (query.isBlank()) {
                    allAnnotations
                } else {
                    allAnnotations.filter {
                        it.note?.contains(query, ignoreCase = true) == true
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
}