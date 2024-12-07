package com.awu0.notetakeoff.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

fun Note.toNoteDetails(): NoteDetails = NoteDetails(
    title = title,
    content = content,
    timestamp = timestamp
)