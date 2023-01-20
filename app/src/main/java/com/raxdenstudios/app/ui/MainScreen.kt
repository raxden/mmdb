package com.raxdenstudios.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.core.navigation.NavigationCommand
import com.raxdenstudios.app.ui.component.MainBottomBar
import com.raxdenstudios.app.ui.graph.mainGraph

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route?.let { route ->
        viewModel.setCurrentRoute(route)
    }

    MainScreen(
        modifier = modifier,
        navController = navController,
        uiState = uiState,
    )
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiState: MainContract.UIState,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (uiState.shouldShowBottomBar)
                MainBottomBar(
                    onNavigateTo = { command -> navController.navigateTo(command) },
                )
        },
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = navController,
            startDestination = MainRoutes.home.value,
        ) {
            mainGraph(navController)
        }
    }
}

private fun NavHostController.navigateTo(command: NavigationCommand) {
    navigate(command.route.value) {
        // Pop up to the start destination of the graph to avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = command.launchSingleTop
        // Restore state when reselecting a previously selected item
        restoreState = command.restoreState
    }
}
