package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.raxdenstudios.app.core.navigation.MainDirections
import com.raxdenstudios.app.feature.search.SearchScreen

@Suppress("UnusedPrivateMember")
fun NavGraphBuilder.searchGraph(
    navController: NavHostController
) {
    composable(route = MainDirections.search.route) {
        SearchScreen()
    }
}
