package com.awu0.notetakeoff.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awu0.notetakeoff.ui.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.model.Note

@Composable
fun HomeScreen(
    onNewNoteButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {

        if (homeUiState.noteList.isEmpty()) {
            NoNotesFoundScreen(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        } else {
            Column {
                NoteList(
                    homeUiState.noteList,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }
        }

        FloatingActionButton(
            onClick = onNewNoteButtonClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)

        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.new_note)
            )
        }
    }
}

@Composable
fun NoNotesFoundScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "No notes found. Add some!"
        )
    }
}

@Composable
fun NoteList(
    noteList: List<Note>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = noteList, key = {it.id}) { item ->
            NoteItem(
                item,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = note.title,
            fontSize = 24.sp,
        )
        Text(
            text = note.content,
        )
    }
}