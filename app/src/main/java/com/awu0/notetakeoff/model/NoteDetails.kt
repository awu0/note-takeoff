package com.awu0.notetakeoff.model

data class NoteDetails(
    val title: String = "",
    val content: String = "",
    val timestamp: Long = 0,
)

fun NoteDetails.toNote(): Note = Note(
    title = title,
    content = content,
)
