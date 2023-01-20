package com.raxdenstudios.app.core.navigation

import androidx.navigation.NamedNavArgument

interface NavigationCommand {

    val arguments: List<NamedNavArgument>
    val route: NavigationRoute

    fun createRoute(parameters: List<String>): NavigationRoute {
        var route = route
        arguments.forEachIndexed { index, argument ->
            if (index < parameters.size) {
                route = NavigationRoute(route.value.replace("{${argument.name}}", parameters[index]))
            }
        }
        return route
    }
}
