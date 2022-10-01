package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.raxdenstudios.app.feature.component.MediaListScreen
import com.raxdenstudios.app.core.navigation.HomeDirections
import com.raxdenstudios.app.core.navigation.MainDirections
import com.raxdenstudios.app.feature.home.component.HomeScreen

fun NavGraphBuilder.homeGraph(
    navController: NavHostController
) {
    navigation(
        route = MainDirections.home.route,
        startDestination = HomeDirections.root.route,
    ) {
        composable(route = HomeDirections.root.route) {
            HomeScreen(
                onNavigateToMediaList = { mediaType, mediaCategory ->
                    val route = HomeDirections.medias.createRoute(listOf(mediaType, mediaCategory))
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = HomeDirections.medias.route,
            arguments = HomeDirections.medias.arguments
        ) {
            MediaListScreen(
                onNavigateToBack = { navController.navigateUp() },
                onNavigateToMedia = { mediaId ->

                }
            )
        }
    }
}
