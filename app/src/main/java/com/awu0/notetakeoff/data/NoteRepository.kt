package com.awu0.notetakeoff.data

import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>

    fun getNote(id: Int): Flow<Note?>

    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun deleteNoteById(id: Int)
}