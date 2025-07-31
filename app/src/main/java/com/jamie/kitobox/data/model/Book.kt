package com.jamie.kitobox.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String?, // Author might not be available from metadata
    val fileUri: String,
    val coverImagePath: String?, // Path to a generated thumbnail
    val progress: Float = 0f,
    val dateAdded: Long = System.currentTimeMillis()
)