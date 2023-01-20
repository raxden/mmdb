package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.app.feature.account.AccountScreen

@Suppress("UnusedPrivateMember")
fun NavGraphBuilder.accountGraph() {
    composable(route = MainRoutes.account.value) {
        AccountScreen()
    }
}
