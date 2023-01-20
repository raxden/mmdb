package com.raxdenstudios.app.ui

import com.raxdenstudios.app.core.navigation.NavigationCommand
import com.raxdenstudios.app.ui.model.BottomBarItemModel
import java.util.UUID

object BottomBarContract {

    data class UIState(
        val items: List<BottomBarItemModel> = BottomBarItemModel.default,
        val events: Set<UIEvent> = emptySet(),
    )

    sealed class UserEvent {

        data class ItemSelected(val item: BottomBarItemModel) : UserEvent()
    }

    sealed class UIEvent {

        val id: String = UUID.randomUUID().toString()

        data class NavigateTo(val command: NavigationCommand) : UIEvent()
    }
}
