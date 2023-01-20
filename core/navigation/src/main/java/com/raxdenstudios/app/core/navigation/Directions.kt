package com.raxdenstudios.app.core.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object MainDirections {

    val home = object : BottomBarNavigationCommand() {
        override val route = MainRoutes.home
    }
    val search = object : BottomBarNavigationCommand() {
        override val route = MainRoutes.search
    }
    val account = object : BottomBarNavigationCommand() {
        override val route = MainRoutes.account
    }
}

object HomeDirections {

    val root = object : SimpleNavigationCommand() {
        override val route = HomeRoutes.home
    }

    val medias = object : SimpleNavigationCommand() {
        override val arguments = listOf(
            navArgument("mediaType") { type = NavType.IntType },
            navArgument("mediaCategory") { type = NavType.IntType }
        )
        override val route = HomeRoutes.medias
    }
}