package com.awu0.notetakeoff.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.awu0.notetakeoff.ui.home.NoteHome

@Composable
fun NoteApp() {

    Scaffold { contentPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(contentPadding)
        ) {
            NoteHome()
        }
    }
}