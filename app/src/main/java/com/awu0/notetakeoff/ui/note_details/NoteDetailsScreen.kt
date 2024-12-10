package com.awu0.notetakeoff.ui.note_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.model.Note
import com.awu0.notetakeoff.model.NoteDetails
import com.awu0.notetakeoff.model.toNote
import com.awu0.notetakeoff.navigation.NavigationDestination
import com.awu0.notetakeoff.ui.AppViewModelProvider
import com.awu0.notetakeoff.ui.NoteAppBar
import com.awu0.notetakeoff.ui.new_note.NoteUiState
import com.awu0.notetakeoff.ui.theme.NoteTakeoffTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object NoteDetailsDestination : NavigationDestination {
    override val route = "note_details"
    override val titleRes = R.string.note_detail

    const val noteIdArg = "noteId"
    val routeWithArgs = "$route/{$noteIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToEditNote: (Int) -> Unit,
    viewModel: NoteDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val noteUiState = viewModel.noteUiState
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var showDetailsDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                coroutineScope.launch {
                    viewModel.updateNote() // save when the app goes to the background
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler {
        coroutineScope.launch {
            viewModel.updateNote() // Save when screen is disposed
            navigateBack()
        }
    }

    Scaffold(
        topBar = {
            NoteAppBar(
                currentScreenTitle = noteUiState.noteDetails.title,
                canNavigateBack = true,
                navigateUp = {
                    coroutineScope.launch {
                        viewModel.updateNote() // Save when screen is disposed
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .drawBehind {
                        val borderColor = Color.LightGray // Border color
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.0f
                        )
                    },
                actions = {
                    NoteDetailsContextMenu(
                        onDetails = { showDetailsDialog = true },
                        onEdit = { navigateToEditNote(noteUiState.noteDetails.id) },
                        onDelete = { showDeleteDialog = true }
                    )
                }
            )

        }
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusRequester.requestFocus()
                    }
            ) {
                NoteDetailsBody(
                    noteUiState = noteUiState,
                    onNoteValueChange = viewModel::updateUiState,
                    onSaveNote = {
                        coroutineScope.launch {
                            viewModel.updateNote()
                        }
                    },
                    focusRequester = focusRequester
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_note_details)))
            }

            if (showDeleteDialog) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        showDeleteDialog = false
                        coroutineScope.launch {
                            viewModel.deleteNote()
                            navigateBack()
                        }
                    },
                    onDeleteCancel = {
                        showDeleteDialog = false
                    }
                )
            } else if (showDetailsDialog) {
                DetailsDialog(
                    note = noteUiState.noteDetails.toNote(),
                    onDone = {
                        showDetailsDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun NoteDetailsBody(
    noteUiState: NoteUiState,
    onNoteValueChange: (NoteDetails) -> Unit,
    onSaveNote: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        TextField(
            value = noteUiState.noteDetails.content,
            onValueChange = { onNoteValueChange(noteUiState.noteDetails.copy(content = it)) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent, // removes the bottom line when focused
                unfocusedIndicatorColor = Color.Transparent, // removes the bottom line when unfocused
            ),
            modifier = Modifier.focusRequester(focusRequester)
                .onFocusChanged {
                    onSaveNote()
                }
        )
    }
}

@Composable
fun NoteDetailsContextMenu(
    onDetails: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { isMenuExpanded = !isMenuExpanded }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more_options)
            )
        }
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }
        ) {
            // details
            DropdownMenuItem(
                text = { Text(stringResource(R.string.note_detail)) },
                onClick = {
                    isMenuExpanded = false
                    onDetails()
                }
            )

            // edit
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit_note)) },
                onClick = {
                    isMenuExpanded = false
                    onEdit()
                }
            )

            // delete
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete_note)) },
                onClick = {
                    isMenuExpanded = false
                    onDelete()
                }
            )
        }
    }
}

@Composable
private fun DetailsDialog(
    note: Note,
    onDone: () -> Unit,
) {
    val dateTimeFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    val dateTimeCreated = remember(note.dateTimeCreated) {
        dateTimeFormatter.format(Date(note.dateTimeCreated))
    }

    val dateTimeUpdated = remember(note.lastUpdated) {
        dateTimeFormatter.format(Date(note.lastUpdated))
    }


    AlertDialog(
        onDismissRequest = onDone,
        title = { Text(stringResource(R.string.note_detail)) },
        text = {
            Column {
                Row {
                    Text(stringResource(R.string.updated) + ": ")
                    Text(text = dateTimeUpdated)
                }
                Row {
                    Text(stringResource(R.string.created) + ": ")
                    Text(text = dateTimeCreated)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDone) {
                Text(stringResource(R.string.done))
            }
        },
    )
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDeleteCancel,
        title = { Text(stringResource(R.string.delete_note_title)) },
        text = { Text(stringResource(R.string.delete_note_confirmation)) },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.delete_note))
            }
        },
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun NoteDetailsScreenPreview() {
    NoteTakeoffTheme {
        NoteDetailsBody(
            noteUiState = NoteUiState(
                noteDetails = NoteDetails(
                    id = 1,
                    title = "Sample Note Title",
                    content = "This is a sample note content for preview.",
                    dateTimeCreated = System.currentTimeMillis(),
                    lastUpdated = System.currentTimeMillis()
                )
            ),
            onNoteValueChange = {},
            onSaveNote = {},
            focusRequester = FocusRequester()
        )
    }
}