package com.raxdenstudios.app.ui

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.navigation.HomeDirections
import com.raxdenstudios.app.core.navigation.NavigationRoute

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
): AppState = AppState(
    navController = navController,
    scaffoldState = scaffoldState,
)

class AppState(
    val navController: NavHostController,
    val scaffoldState: ScaffoldState,
) {

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateToTopLevelDestination(route: NavigationRoute) {
        val navOptions = navOptions {
            // Pop up to the start destination of the graph to avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
        navController.navigate(route.value, navOptions)
    }

    fun navigateToMedia(
        mediaId: MediaId,
        mediaType: MediaType,
    ) {
        val params = listOf(mediaId.value.toString(), mediaType.value.toString())
        val route = HomeDirections.media.createRoute(params)
        navController.navigate(route.value)
    }

    fun navigateToMedias(
        mediaType: MediaType,
        mediaCategory: MediaCategory,
    ) {
        val params = listOf(mediaType.value.toString(), mediaCategory.value.toString())
        val route = HomeDirections.medias.createRoute(params)
        navController.navigate(route.value)
    }
}
