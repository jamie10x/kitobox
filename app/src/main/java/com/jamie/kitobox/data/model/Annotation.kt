package com.jamie.kitobox.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// The class name is now BookAnnotation.
// The table name remains "annotations" for database stability.
@Entity(
    tableName = "annotations",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["bookId"])]
)
data class BookAnnotation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int,
    val pageNumber: Int,
    val note: String?,
    val highlightData: String?,
    val type: AnnotationType,
    val dateAdded: Long = System.currentTimeMillis()
)

enum class AnnotationType {
    HIGHLIGHT, NOTE
}