package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.raxdenstudios.app.core.navigation.HomeDirections
import com.raxdenstudios.app.core.navigation.HomeRoutes
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.feature.component.MediasScreen
import com.raxdenstudios.app.feature.detail.component.MediaScreen
import com.raxdenstudios.app.feature.home.component.HomeScreen
import com.raxdenstudios.app.ui.AppState

fun NavGraphBuilder.homeGraph(
    appState: AppState,
) {
    navigation(
        route = MainRoutes.home.value,
        startDestination = HomeRoutes.home.value,
    ) {
        composable(route = HomeRoutes.home.value) {
            HomeScreen(
                scaffoldState = appState.scaffoldState,
                onNavigateToMedias = { mediaType, mediaCategory ->
                    appState.navigateToMedias(mediaType, mediaCategory)
                },
                onNavigateToMedia = { mediaId, mediaType ->
                    appState.navigateToMedia(mediaId, mediaType)
                }
            )
        }
        composable(
            route = HomeRoutes.medias.value,
            arguments = HomeDirections.medias.arguments
        ) {
            MediasScreen(
                onNavigateToBack = { appState.navigateUp() },
                onNavigateToMedia = { mediaId, mediaType ->
                    appState.navigateToMedia(mediaId, mediaType)
                }
            )
        }
        composable(
            route = HomeRoutes.media.value,
            arguments = HomeDirections.media.arguments
        ) {
            MediaScreen(
                onNavigateToBack = { appState.navigateUp() }
            )
        }
    }
}
