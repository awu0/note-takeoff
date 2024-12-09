package com.awu0.notetakeoff.ui.note_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.model.Note
import com.awu0.notetakeoff.model.toNote
import com.awu0.notetakeoff.navigation.NavigationDestination
import com.awu0.notetakeoff.ui.AppViewModelProvider
import com.awu0.notetakeoff.ui.NoteAppBar
import com.awu0.notetakeoff.ui.new_note.NoteDetailsViewModel
import com.awu0.notetakeoff.ui.theme.NoteTakeoffTheme
import kotlinx.coroutines.launch

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
    val uiState = viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            NoteAppBar(
                currentScreenTitle = uiState.value.noteDetails.title,
                canNavigateBack = true,
                navigateUp = navigateBack,
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
                        onEdit = { navigateToEditNote(uiState.value.noteDetails.id) },
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
                    .verticalScroll(scrollState)
            ) {
                NoteDetailsBody(
                    note = uiState.value.noteDetails.toNote(),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                )
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
            }
        }
    }
}

@Composable
fun NoteDetailsBody(
    note: Note,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = note.content,
        )
    }
}

@Composable
fun NoteDetailsContextMenu(
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
            Note(
                title = "My note is this",
                content = "The body of my note."
            )
        )
    }
}