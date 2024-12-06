package com.awu0.notetakeoff.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.awu0.notetakeoff.NoteApplication

/**
 * Provides the entire app with all the view models
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NoteHomeViewModel(noteApplication().container.noteRepository)
        }
    }
}

fun CreationExtras.noteApplication(): NoteApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)
