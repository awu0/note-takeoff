package com.awu0.notetakeoff.data

import com.awu0.notetakeoff.dao.NoteDao
import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(private val noteDao: NoteDao): NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
}
