package com.awu0.notetakeoff.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awu0.notetakeoff.data.NoteRepository
import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(val noteList: List<Note> = listOf())

class HomeViewModel(noteRepository: NoteRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        noteRepository.getAllNotes().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}