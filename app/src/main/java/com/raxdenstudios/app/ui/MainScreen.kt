package com.raxdenstudios.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.raxdenstudios.app.core.navigation.MainDirections
import com.raxdenstudios.app.ui.component.MainBottomBar
import com.raxdenstudios.app.ui.graph.mainGraph

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            is MainContract.UIEvent.NavigateTo ->
                navController.navigate(event.command.route)
        }
        viewModel.eventConsumed(event)
    }

    MainScreen(
        modifier = modifier,
        navController = navController,
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is MainContract.UserEvent.BottomBarItemSelected -> viewModel.setUserEvent(event)
            }
        },
    )
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiState: MainContract.UIState,
    onEvent: (event: MainContract.UserEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            MainBottomBar(
                items = uiState.items,
                onItemClick = { item ->
                    onEvent(MainContract.UserEvent.BottomBarItemSelected(item))
                },
            )
        },
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = navController,
            startDestination = MainDirections.home.route,
        ) {
            mainGraph(navController)
        }
    }
}
