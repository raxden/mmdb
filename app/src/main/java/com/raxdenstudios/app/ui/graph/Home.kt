package com.raxdenstudios.app.ui.graph

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.navigation.HomeDirections
import com.raxdenstudios.app.core.navigation.HomeRoutes
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.feature.component.MediasScreen
import com.raxdenstudios.app.feature.detail.component.MediaScreen
import com.raxdenstudios.app.feature.home.component.HomeScreen

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
) {
    navigation(
        route = MainRoutes.home.value,
        startDestination = HomeRoutes.home.value,
    ) {
        composable(route = HomeRoutes.home.value) {
            HomeScreen(
                scaffoldState = scaffoldState,
                onNavigateToMedias = { mediaType, mediaCategory ->
                    navController.navigateToMedias(mediaType, mediaCategory)
                },
                onNavigateToMedia = { mediaId, mediaType ->
                    navController.navigateToMedia(mediaId, mediaType)
                }
            )
        }
        composable(
            route = HomeRoutes.medias.value,
            arguments = HomeDirections.medias.arguments
        ) {
            MediasScreen(
                onNavigateToBack = { navController.navigateUp() },
                onNavigateToMedia = { mediaId, mediaType ->
                    navController.navigateToMedia(mediaId, mediaType)
                }
            )
        }
        composable(
            route = HomeRoutes.media.value,
            arguments = HomeDirections.media.arguments
        ) {
            MediaScreen(
                onNavigateToBack = { navController.navigateUp() }
            )
        }
    }
}

private fun NavHostController.navigateToMedia(
    mediaId: MediaId,
    mediaType: MediaType,
) {
    val params = listOf(mediaId.value.toString(), mediaType.value.toString())
    val route = HomeDirections.media.createRoute(params)
    navigate(route.value)
}

private fun NavHostController.navigateToMedias(
    mediaType: MediaType,
    mediaCategory: MediaCategory,
) {
    val params = listOf(mediaType.value.toString(), mediaCategory.value.toString())
    val route = HomeDirections.medias.createRoute(params)
    navigate(route.value)
}

