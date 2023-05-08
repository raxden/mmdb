package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import com.raxdenstudios.app.ui.AppState

fun NavGraphBuilder.mainGraph(
    appState: AppState,
) {
    homeGraph(appState)
    searchGraph(appState)
    accountGraph()
}
