package com.raxdenstudios.app.core.navigation

import androidx.navigation.NamedNavArgument

object MainDirections {

    val home = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val route = "mainHome"
    }
    val search = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val route = "mainSearch"
    }
    val account = object : NavigationCommand {
        override val arguments = emptyList<NamedNavArgument>()
        override val route = "mainAccount"
    }
}
