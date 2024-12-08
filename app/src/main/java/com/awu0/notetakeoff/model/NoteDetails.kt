package com.awu0.notetakeoff.model

data class NoteDetails(
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val timestamp: Long = 0,
)

fun NoteDetails.toNote(): Note = Note(
    id = id,
    title = title,
    content = content,
)
