package com.awu0.notetakeoff.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.rememberNavController
import com.awu0.notetakeoff.R
import com.awu0.notetakeoff.navigation.NoteNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAppBar(
    currentScreenTitle: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    actions: @Composable() (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    TopAppBar(
        title = {
            Text(
                text = currentScreenTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
            )
        },
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
        },
        actions = {
            actions?.invoke()
        }
    )
}

@Composable
fun NoteApp() {
    val navController = rememberNavController()

    NoteNavGraph(
        navController = navController,
    )
}