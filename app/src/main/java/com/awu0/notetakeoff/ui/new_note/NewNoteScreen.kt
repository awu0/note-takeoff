package com.awu0.notetakeoff.ui.new_note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun NewNoteScreen(
    navigateBackToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewNoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
        NoteEntryBody(
            noteUiState = viewModel.noteUiState,
            onNoteValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveNote()
                    navigateBackToHomeScreen()
                }
            }
        )
    }
}

@Composable
fun NoteEntryBody(
    noteUiState: NewNoteUiState,
    onNoteValueChange: (NoteDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val noteDetails = noteUiState.noteDetails

    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium))
    ) {
        // note title
        OutlinedTextField(
            label = { Text(stringResource(R.string.new_note_title)) },
            value = noteUiState.noteDetails.title,
            onValueChange = { onNoteValueChange(noteDetails.copy(title = it)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // note content
        OutlinedTextField(
            label = { Text(stringResource(R.string.new_note_content)) },
            value = noteUiState.noteDetails.content,
            onValueChange = { onNoteValueChange(noteDetails.copy(content = it)) },
            minLines = 10,
            modifier = Modifier.fillMaxWidth()
        )

        // save button
        Button(
            onClick = onSaveClick,
            enabled = noteUiState.isEntryValid
        ) {
            Text(text = stringResource(R.string.new_note_save))
        }
    }

}