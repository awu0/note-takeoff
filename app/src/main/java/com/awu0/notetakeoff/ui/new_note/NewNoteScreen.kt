package com.awu0.notetakeoff.ui.new_note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.model.NoteDetails
import com.awu0.notetakeoff.navigation.NavigationDestination
import com.awu0.notetakeoff.ui.AppViewModelProvider
import com.awu0.notetakeoff.ui.NoteAppBar
import kotlinx.coroutines.launch

object NewNoteDestination : NavigationDestination {
    override val route = "new_note"
    override val titleRes = R.string.add_new_note
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: NewNoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            NoteAppBar(
                currentScreenTitle = stringResource(NewNoteDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->

        val coroutineScope = rememberCoroutineScope()

        Box(modifier = modifier.padding(contentPadding)) {
            NoteEntryBody(
                noteUiState = viewModel.noteUiState,
                onNoteValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.saveNote()
                        navigateBack()
                    }
                }
            )
        }
    }
}

@Composable
fun NoteEntryBody(
    noteUiState: NoteUiState,
    onNoteValueChange: (NoteDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val noteDetails = noteUiState.noteDetails

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // note title
        OutlinedTextField(
            label = { Text(stringResource(R.string.new_note_title)) },
            value = noteUiState.noteDetails.title,
            onValueChange = { if (it.length <= 50) onNoteValueChange(noteDetails.copy(title = it)) },
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

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        // save button
        Button(
            onClick = onSaveClick,
            enabled = noteUiState.isEntryValid
        ) {
            Text(text = stringResource(R.string.new_note_save))
        }
    }
}