package com.awu0.notetakeoff.ui.new_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.awu0.notetakeoff.data.NoteRepository
import com.awu0.notetakeoff.model.NoteDetails
import com.awu0.notetakeoff.model.toNote

class NewNoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    var noteUiState by mutableStateOf(NoteUiState())
        private set

    fun updateUiState(noteDetails: NoteDetails) {
        noteUiState = NoteUiState(
            noteDetails = noteDetails,
            isEntryValid = validateInput(noteDetails)
        )
    }

    suspend fun saveNote() {
        if (!noteUiState.isEntryValid) return

        noteRepository.insertNote(noteUiState.noteDetails.toNote())
    }

    private fun validateInput(uiState: NoteDetails = noteUiState.noteDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }
}

data class NoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val isEntryValid: Boolean = false
)
