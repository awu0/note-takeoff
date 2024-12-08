package com.awu0.notetakeoff.ui.edit_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awu0.notetakeoff.data.NoteRepository
import com.awu0.notetakeoff.model.NoteDetails
import com.awu0.notetakeoff.model.toNote
import com.awu0.notetakeoff.model.toNoteDetails
import com.awu0.notetakeoff.ui.new_note.NoteUiState
import com.awu0.notetakeoff.ui.note_details.NoteDetailsDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {

    var noteUiState by mutableStateOf(NoteUiState())
        private set

    private val noteId: Int = checkNotNull(savedStateHandle[NoteDetailsDestination.noteIdArg])

    init {
        viewModelScope.launch {
            noteUiState = NoteUiState(
                noteDetails = noteRepository.getNote(noteId)
                    .filterNotNull()
                    .first()
                    .toNoteDetails()
            )
        }
    }

    fun updateUiState(noteDetails: NoteDetails) {
        noteUiState = NoteUiState(
            noteDetails = noteDetails,
            isEntryValid = validateInput(noteDetails)
        )
    }

    suspend fun updateNote() {
        if (!noteUiState.isEntryValid) return

        noteRepository.updateNote(noteUiState.noteDetails.toNote())
    }

    private fun validateInput(uiState: NoteDetails = noteUiState.noteDetails): Boolean {
        return with(uiState) {
            title.isNotBlank()
        }
    }
}
