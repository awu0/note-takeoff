package com.awu0.notetakeoff.ui.new_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awu0.notetakeoff.data.NoteRepository
import com.awu0.notetakeoff.model.NoteDetails
import com.awu0.notetakeoff.model.toNoteDetails
import com.awu0.notetakeoff.ui.note_details.NoteDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val noteId: Int = checkNotNull(savedStateHandle[NoteDetailsDestination.noteIdArg])

    val uiState: StateFlow<NoteDetailsUiState> =
        noteRepository.getNote(noteId)
            .filterNotNull()
            .map {
                NoteDetailsUiState(noteDetails = it.toNoteDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = NoteDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class NoteDetailsUiState(
    val noteDetails: NoteDetails = NoteDetails(),
)
