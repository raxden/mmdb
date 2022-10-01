package com.raxdenstudios.app.core.navigation

import androidx.navigation.NamedNavArgument

interface NavigationCommand {

    val arguments: List<NamedNavArgument>
    val route: String

    fun createRoute(parameters: List<String>): String {
        var route = route
        arguments.forEachIndexed { index, argument ->
            if (index < parameters.size) {
                route = route.replace("{${argument.name}}", parameters[index])
            }
        }
        return route
    }
}
