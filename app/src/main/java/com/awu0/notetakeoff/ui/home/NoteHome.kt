package com.awu0.notetakeoff.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.awu0.notetakeoff.ui.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.model.Note

@Composable
fun NoteHome(
    modifier: Modifier = Modifier,
    viewModel: NoteHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val noteHomeUiState by viewModel.noteHomeUiState.collectAsState()

    Column {
        Text("HELLO")
        NoteList(noteHomeUiState.noteList)
    }
}

@Composable
fun NoteList(
    noteList: List<Note>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(items = noteList, key = {it.id}) { item ->
            Text(
                text = item.title
            )
            Text(
                text = item.content
            )
        }
    }
}