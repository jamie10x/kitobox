package com.jamie.kitobox.data.repository

import com.jamie.kitobox.data.local.BookDao
import com.jamie.kitobox.data.model.Book
import kotlinx.coroutines.flow.Flow

open class BookRepository(private val bookDao: BookDao) {

    fun getAllBooks(): Flow<List<Book>> {
        return bookDao.getAllBooks()
    }

    open fun getBooksWithAnnotations(): Flow<List<Book>> {
        return bookDao.getBooksWithAnnotations()
    }

    suspend fun insertBook(book: Book) {
        bookDao.insertBook(book)
    }

    suspend fun getBookById(id: Int): Book? {
        return bookDao.getBookById(id)
    }
}