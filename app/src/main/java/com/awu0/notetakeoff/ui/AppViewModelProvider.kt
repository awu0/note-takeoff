package com.awu0.notetakeoff.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.awu0.notetakeoff.NoteApplication
import com.awu0.notetakeoff.ui.edit_note.EditNoteViewModel
import com.awu0.notetakeoff.ui.home.HomeViewModel
import com.awu0.notetakeoff.ui.new_note.NewNoteViewModel
import com.awu0.notetakeoff.ui.note_details.NoteDetailsViewModel

/**
 * Provides the entire app with all the view models
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(noteApplication().container.noteRepository)
        }

        initializer {
            NewNoteViewModel(noteApplication().container.noteRepository)
        }

        initializer {
            NoteDetailsViewModel(
                this.createSavedStateHandle(),
                noteApplication().container.noteRepository
            )
        }

        initializer {
            EditNoteViewModel(
                this.createSavedStateHandle(),
                noteApplication().container.noteRepository
            )
        }
    }
}

fun CreationExtras.noteApplication(): NoteApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)
