package com.awu0.notetakeoff.ui

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.ui.home.NoteHome

enum class NoteScreen(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    NewNote(title = R.string.new_note)
}

@Composable
fun NoteApp() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = NoteScreen.valueOf(
        backStackEntry?.destination?.route ?: NoteScreen.Home.name
    )

    Scaffold { contentPadding ->

        NavHost(
            navController = navController,
            startDestination = NoteScreen.Home.name,
        ) {
            composable(route = NoteScreen.Home.name) {
                NoteHome(
                    onNewNoteButtonClicked = {
                        navController.navigate(NoteScreen.NewNote.name)
                    },
                    modifier = Modifier.fillMaxSize().padding(contentPadding)
                )
            }

            composable(route = NoteScreen.NewNote.name) {
                Text(
                    text = "NEW NOTE HERE",
                    fontSize = 64.sp
                )
            }
        }
    }
}