package com.raxdenstudios.app.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object HomeDirections {

    val root = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val route = "home"
    }

    val medias = object : NavigationCommand {
        override val arguments = listOf(
            navArgument("mediaType") { type = NavType.IntType },
            navArgument("mediaCategory") { type = NavType.IntType }
        )
        override val route = "medias/{mediaType}/{mediaCategory}"
    }
}
