package com.awu0.notetakeoff.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,

    @ColumnInfo(name = "date_time_created")
    val dateTimeCreated: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
) {
    init {
        require(title.length <= 50) { "Title cannot exceed 50 characters" }
    }
}

fun Note.toNoteDetails(): NoteDetails = NoteDetails(
    id = id,
    title = title,
    content = content,
    dateTimeCreated = dateTimeCreated,
    lastUpdated = lastUpdated
)