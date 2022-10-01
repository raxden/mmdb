package com.raxdenstudios.app.ui.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.raxdenstudios.app.core.navigation.MainDirections
import com.raxdenstudios.app.feature.account.AccountScreen

@Suppress("UnusedPrivateMember")
fun NavGraphBuilder.accountGraph(
    navController: NavHostController
) {
    composable(route = MainDirections.account.route) {
        AccountScreen()
    }
}
