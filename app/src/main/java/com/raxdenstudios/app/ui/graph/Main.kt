package com.raxdenstudios.app.ui.graph

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
) {
    homeGraph(navController, scaffoldState)
    searchGraph()
    accountGraph()
}
