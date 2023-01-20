package com.raxdenstudios.app.core.navigation

import androidx.navigation.NamedNavArgument

interface NavigationCommand {

    val arguments: List<NamedNavArgument>
    val route: NavigationRoute

    // Avoid multiple copies of the same destination when reselecting the same item
    val launchSingleTop: Boolean

    // Restore state when reselecting a previously selected item
    val restoreState: Boolean

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

abstract class SimpleNavigationCommand : NavigationCommand {

    override val arguments: List<NamedNavArgument> = emptyList()
    override val launchSingleTop: Boolean = false
    override val restoreState: Boolean = false
}

abstract class BottomBarNavigationCommand : NavigationCommand {

    override val arguments: List<NamedNavArgument> = emptyList()
    override val launchSingleTop: Boolean = true
    override val restoreState: Boolean = true
}
