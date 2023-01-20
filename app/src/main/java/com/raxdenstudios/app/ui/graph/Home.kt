package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.raxdenstudios.app.core.navigation.HomeDirections
import com.raxdenstudios.app.core.navigation.HomeRoutes
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.feature.component.MediaListScreen
import com.raxdenstudios.app.feature.home.component.HomeScreen

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
) {
    navigation(
        route = MainRoutes.home.value,
        startDestination = HomeRoutes.home.value,
    ) {
        composable(route = HomeRoutes.home.value) {
            HomeScreen(
                onNavigateToMediaList = { mediaType, mediaCategory ->
                    val route = HomeDirections.medias.createRoute(listOf(mediaType, mediaCategory))
                    navController.navigate(route.value)
                }
            )
        }
        composable(
            route = HomeRoutes.medias.value,
            arguments = HomeDirections.medias.arguments
        ) {
            MediaListScreen(
                onNavigateToBack = { navController.navigateUp() },
                onNavigateToMedia = { mediaId ->
                    val route = HomeDirections.media.createRoute(listOf(mediaId.value.toString()))
                    navController.navigate(route.value)
                }
            )
        }
    }
}
