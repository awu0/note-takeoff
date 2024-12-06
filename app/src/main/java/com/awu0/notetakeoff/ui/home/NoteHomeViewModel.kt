package com.awu0.notetakeoff.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awu0.notetakeoff.data.NoteRepository
import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class NoteHomeUiState(val noteList: List<Note> = listOf())

class NoteHomeViewModel(noteRepository: NoteRepository) : ViewModel() {

    val noteHomeUiState: StateFlow<NoteHomeUiState> =
        noteRepository.getAllNotes().map { NoteHomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteHomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}