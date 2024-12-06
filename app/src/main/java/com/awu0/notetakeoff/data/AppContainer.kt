package com.awu0.notetakeoff.data

import android.content.Context

/**
 * App container for dependency injection.
 */
interface AppContainer {
    val noteRepository: NoteRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val noteRepository: NoteRepository by lazy {
        OfflineNoteRepository(NoteDatabase.getDatabase(context).noteDao())
    }
}