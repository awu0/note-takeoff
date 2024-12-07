package com.awu0.notetakeoff.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.ui.home.HomeScreen
import com.awu0.notetakeoff.ui.new_note.NewNoteScreen

enum class NoteScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    NewNote(title = R.string.new_note)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAppBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = NoteScreen.valueOf(
        backStackEntry?.destination?.route ?: NoteScreen.Home.name
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            NoteAppBar(
                currentScreenTitle = currentScreen.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = NoteScreen.Home.name,
        ) {
            composable(route = NoteScreen.Home.name) {
                HomeScreen(
                    onNewNoteButtonClicked = {
                        navController.navigate(NoteScreen.NewNote.name)
                    },
                    modifier = Modifier.fillMaxSize().padding(contentPadding)
                )
            }

            composable(route = NoteScreen.NewNote.name) {
                NewNoteScreen(
                    navigateBackToHomeScreen = { navController.navigate(NoteScreen.Home.name) },
                    modifier = Modifier.fillMaxSize().padding(contentPadding)
                )
            }
        }
    }
}