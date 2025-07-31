package com.jamie.kitobox.data.repository

import com.jamie.kitobox.data.local.AnnotationDao
import com.jamie.kitobox.data.model.BookAnnotation
import kotlinx.coroutines.flow.Flow

class AnnotationRepository(private val annotationDao: AnnotationDao) {
    fun getAnnotationsForBook(bookId: Int): Flow<List<BookAnnotation>> {
        return annotationDao.getAnnotationsForBook(bookId)
    }

    suspend fun saveAnnotation(annotation: BookAnnotation) {
        annotationDao.insertAnnotation(annotation)
    }
}