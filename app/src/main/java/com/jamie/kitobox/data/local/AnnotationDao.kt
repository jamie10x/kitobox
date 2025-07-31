package com.jamie.kitobox.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamie.kitobox.data.model.BookAnnotation
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnotationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnotation(annotation: BookAnnotation)

    @Query("SELECT * FROM annotations WHERE bookId = :bookId ORDER BY pageNumber ASC")
    fun getAnnotationsForBook(bookId: Int): Flow<List<BookAnnotation>>
}