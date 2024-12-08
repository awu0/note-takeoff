package com.awu0.notetakeoff.navigation

import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.awu0.notetakeoff.ui.edit_note.EditNoteDestination
import com.awu0.notetakeoff.ui.edit_note.EditNoteScreen
import com.awu0.notetakeoff.ui.home.HomeScreen
import com.awu0.notetakeoff.ui.home.HomeScreenDestination
import com.awu0.notetakeoff.ui.new_note.NewNoteDestination
import com.awu0.notetakeoff.ui.new_note.NewNoteScreen
import com.awu0.notetakeoff.ui.note_details.NoteDetailsDestination
import com.awu0.notetakeoff.ui.note_details.NoteDetailsScreen

@Composable
fun NoteNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeScreenDestination.route) {
            HomeScreen(
                onNewNoteButtonClicked = { navController.navigate(NewNoteDestination.route) },
                navigateToNoteUpdate = { navController.navigate("${NoteDetailsDestination.route}/${it}") }
            )
        }

        composable(route = NewNoteDestination.route) {
            NewNoteScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = NoteDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(NoteDetailsDestination.noteIdArg) {
                type = NavType.IntType
            })
        ) {
            NoteDetailsScreen(
                navigateBack = { navController.navigateUp() },
                navigateToEditNote = { navController.navigate("${EditNoteDestination.route}/${it}") },
            )
        }

        composable(
            route = EditNoteDestination.routeWithArgs,
            arguments = listOf(navArgument(NoteDetailsDestination.noteIdArg) {
                type = NavType.IntType
            })
        ) {
            EditNoteScreen(
                navigateBack = { navController.navigateUp() },

            )
        }
    }
}