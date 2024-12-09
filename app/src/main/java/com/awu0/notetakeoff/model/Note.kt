package com.awu0.notetakeoff.model

import androidx.compose.ui.res.stringResource
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.awu0.notetakeoff.R

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    init {
        require(title.length <= 50) { "Title cannot exceed 50 characters" }
    }
}

fun Note.toNoteDetails(): NoteDetails = NoteDetails(
    id = id,
    title = title,
    content = content,
    timestamp = timestamp
)