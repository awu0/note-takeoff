package com.awu0.notetakeoff.data

import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
}