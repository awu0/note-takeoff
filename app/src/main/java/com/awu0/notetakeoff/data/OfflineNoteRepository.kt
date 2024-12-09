package com.awu0.notetakeoff.data

import com.awu0.notetakeoff.dao.NoteDao
import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.Flow

class OfflineNoteRepository(private val noteDao: NoteDao): NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override fun getNote(id: Int): Flow<Note?> = noteDao.getNote(id)

    override suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}
