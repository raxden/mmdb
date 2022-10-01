package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    homeGraph(navController)
    searchGraph(navController)
    accountGraph(navController)
}
