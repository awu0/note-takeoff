package com.awu0.notetakeoff.ui.note_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.model.Note
import com.awu0.notetakeoff.model.toNote
import com.awu0.notetakeoff.navigation.NavigationDestination
import com.awu0.notetakeoff.ui.AppViewModelProvider
import com.awu0.notetakeoff.ui.NoteAppBar
import com.awu0.notetakeoff.ui.new_note.NoteDetailsViewModel

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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            NoteAppBar(
                currentScreenTitle = NoteDetailsDestination.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->

        Box(modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(scrollState)) {

            NoteDetailsBody(
                note = uiState.value.noteDetails.toNote(),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )

            FloatingActionButton(
                onClick = { navigateToEditNote },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(R.dimen.padding_medium))

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_note)
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
            text = note.title,
            fontSize = 24.sp,
        )
        Text(
            text = note.content,
        )
    }
}