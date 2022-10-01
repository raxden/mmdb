package com.raxdenstudios.app.core.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationManager {

    private val _commands = MutableStateFlow(MainDirections.home)
    val commands: StateFlow<NavigationCommand> = _commands.asStateFlow()

    fun navigate(command: NavigationCommand) {
        _commands.value = command
    }
}
