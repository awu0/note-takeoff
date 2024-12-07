package com.awu0.notetakeoff.navigation

interface NavigationDestination {
    /**
     * Identifies the path for a composable
     */
    val route: String

    /**
     * String resource that contains the title for the nav bar
     */
    val titleRes: Int
}