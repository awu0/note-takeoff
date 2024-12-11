package com.awu0.notetakeoff.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.model.Note
import com.awu0.notetakeoff.navigation.NavigationDestination
import com.awu0.notetakeoff.ui.AppViewModelProvider
import com.awu0.notetakeoff.ui.theme.NoteTakeoffTheme

object HomeScreenDestination : NavigationDestination {
    override val route = "home_screen"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewNoteButtonClicked: () -> Unit,
    navigateToNoteUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val filteredNotes = homeUiState.noteList.filter { note ->
        note.title.contains(
            viewModel.searchQuery.trim(),
            ignoreCase = true
        ) || note.content.contains(viewModel.searchQuery.trim(), ignoreCase = true)
    }.sortedByDescending { it.lastUpdated }

    Scaffold(
        topBar = {
            HomeSearchBar(
                searchQuery = viewModel.searchQuery,
                updateQuery = viewModel::updateQuery,
                scrollBehavior = scrollBehavior
            )
        }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            if (homeUiState.noteList.isEmpty()) {
                NoNotesFoundScreen(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            } else {
                NoteList(
                    noteList = filteredNotes,
                    onNoteClick = navigateToNoteUpdate,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }

            FloatingActionButton(
                onClick = onNewNoteButtonClicked,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(R.dimen.padding_medium))

            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_new_note)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    searchQuery: String,
    updateQuery: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { updateQuery(it) },
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    placeholder = { Text(stringResource(R.string.search_notes)) },
                    modifier = modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.search_bar_corners))),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent, // removes the bottom line when focused
                        unfocusedIndicatorColor = Color.Transparent, // removes the bottom line when unfocused
                    ),
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteList(
    noteList: List<Note>, onNoteClick: (Int) -> Unit, modifier: Modifier = Modifier
) {

    val selectedNotes = remember { mutableStateOf(setOf<Int>()) }

    BackHandler(enabled = selectedNotes.value.isNotEmpty()) {
        selectedNotes.value = emptySet()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        items(items = noteList, key = { it.id }) { item ->
            NoteItem(item,
                selected = selectedNotes.value.contains(item.id),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.item_height))
                    .combinedClickable(
                        onClick = {
                            if (selectedNotes.value.isNotEmpty()) {
                                selectedNotes.value = if (selectedNotes.value.contains(item.id)) {
                                    selectedNotes.value - item.id
                                } else {
                                    selectedNotes.value + item.id
                                }
                            } else {
                                onNoteClick(item.id)
                            }
                        },
                        onLongClick = {
                            selectedNotes.value += item.id
                        }
                    )
            )
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Card(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Text(
                        text = note.content, maxLines = 6, overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = note.title,
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W500,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.Gray, shape = CircleShape)
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(R.dimen.padding_extra_small))
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NoteListPreview() {
    val sampleNotes = listOf(
        Note(id = 1, title = "First Note", content = "This is the first note."),
        Note(
            id = 2, title = "Second Note", content = "Here's some more content for the second note."
        ),
        Note(
            id = 3,
            title = "Third Note",
            content = "The third note has a lot of interesting details. The third note has a lot of interesting details."
        ),
    )

    NoteTakeoffTheme {
        NoteList(noteList = sampleNotes, onNoteClick = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeSearchBarPreview() {
    NoteTakeoffTheme {
        HomeSearchBar("Search", {})
    }
}