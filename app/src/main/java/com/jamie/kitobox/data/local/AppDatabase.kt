package com.jamie.kitobox.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jamie.kitobox.data.model.BookAnnotation
import com.jamie.kitobox.data.model.Book

@Database(entities = [Book::class, BookAnnotation::class], version = 2, exportSchema = false) // <-- UPDATED ENTITY
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun annotationDao(): AnnotationDao
}