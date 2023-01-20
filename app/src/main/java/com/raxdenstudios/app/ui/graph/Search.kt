package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.feature.search.SearchScreen

@Suppress("UnusedPrivateMember")
fun NavGraphBuilder.searchGraph() {
    composable(route = MainRoutes.search.value) {
        SearchScreen()
    }
}
