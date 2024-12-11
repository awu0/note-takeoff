package com.awu0.notetakeoff.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awu0.notetakeoff.data.NoteRepository
import com.awu0.notetakeoff.model.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(val noteList: List<Note> = listOf())

class HomeViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    var selectedNotes by mutableStateOf(setOf<Int>())
        private set

    val homeUiState: StateFlow<HomeUiState> =
        noteRepository.getAllNotes().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    fun updateQuery(query: String) {
        searchQuery = query
    }

    fun resetSelectedNotes() {
        selectedNotes = emptySet()
    }

    fun addSelectedNote(noteId: Int) {
        selectedNotes += noteId
    }

    fun removeSelectedNote(noteId: Int) {
        selectedNotes -= noteId
    }

    suspend fun deleteSelectedNotes() {
        selectedNotes.forEach { id->
            noteRepository.deleteNoteById(id)
        }

        resetSelectedNotes()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}