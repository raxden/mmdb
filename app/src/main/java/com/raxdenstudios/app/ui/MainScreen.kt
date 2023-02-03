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
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
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
import com.raxdenstudios.app.core.navigation.NavigationRoute
import com.raxdenstudios.app.ui.component.MainBottomBar
import com.raxdenstudios.app.ui.graph.mainGraph

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
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
        scaffoldState = scaffoldState,
        uiState = uiState,
    )
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    uiState: MainContract.UIState,
) {
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        bottomBar = {
            if (uiState.shouldShowBottomBar) {
                Column {
                    MainBottomBar(
                        onNavigateTo = { route -> navController.navigateTo(route) },
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
            navController = navController,
            startDestination = MainRoutes.home.value,
        ) {
            mainGraph(navController, scaffoldState)
        }
    }
}

private fun NavHostController.navigateTo(route: NavigationRoute) {
    navigate(route.value) {
        // Pop up to the start destination of the graph to avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}
