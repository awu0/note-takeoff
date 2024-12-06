package com.awu0.notetakeoff.dao

import androidx.room.Dao
import androidx.room.Query
import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>
}