package com.raxdenstudios.app.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object MainDirections {

    val home = object : NavigationCommand {
        override val route = MainRoutes.home
        override val arguments = emptyList<NamedNavArgument>()
    }
    val search = object : NavigationCommand {
        override val route = MainRoutes.search
        override val arguments = emptyList<NamedNavArgument>()
    }
    val account = object : NavigationCommand {
        override val route = MainRoutes.account
        override val arguments = emptyList<NamedNavArgument>()
    }
}

object HomeDirections {

    val root = object : NavigationCommand {
        override val route = HomeRoutes.home
        override val arguments = emptyList<NamedNavArgument>()
    }

    val medias = object : NavigationCommand {
        override val arguments = listOf(
            navArgument("mediaType") { type = NavType.IntType },
            navArgument("mediaCategory") { type = NavType.IntType }
        )
        override val route = HomeRoutes.medias
    }

    val media = object : NavigationCommand {
        override val route = HomeRoutes.media
        override val arguments = listOf(
            navArgument("mediaId") { type = NavType.IntType },
            navArgument("mediaType") { type = NavType.IntType }
        )
    }
}
