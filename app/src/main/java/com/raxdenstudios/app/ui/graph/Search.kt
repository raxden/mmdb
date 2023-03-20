package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.feature.search.SearchScreen
import com.raxdenstudios.app.ui.AppState

@Suppress("UnusedPrivateMember")
fun NavGraphBuilder.searchGraph(
    appState: AppState,
) {
    composable(route = MainRoutes.search.value) {
        SearchScreen(
            onNavigateToMedia = { mediaId, mediaType -> appState.navigateToMedia(mediaId, mediaType) },
        )
    }
}
