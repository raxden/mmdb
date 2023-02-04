package com.raxdenstudios.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.ui.component.MainBottomBar
import com.raxdenstudios.app.ui.graph.mainGraph

@Composable
fun MainScreen(
    appState: AppState = rememberAppState(),
    viewModel: MainViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()

    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route?.let { route ->
        viewModel.setCurrentRoute(route)
    }

    MainScreen(
        appState = appState,
        uiState = uiState,
    )
}

@Composable
private fun MainScreen(
    appState: AppState,
    uiState: MainContract.UIState,
) {
    Scaffold(
        modifier = Modifier,
        scaffoldState = appState.scaffoldState,
        bottomBar = {
            if (uiState.shouldShowBottomBar) {
                Column {
                    MainBottomBar(
                        onNavigateTo = { route -> appState.navigateToTopLevelDestination(route) },
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsBottomHeight(WindowInsets.navigationBars)
                            .background(color = MaterialTheme.colors.background)
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = appState.navController,
            startDestination = MainRoutes.home.value,
        ) {
            mainGraph(appState)
        }
    }
}
