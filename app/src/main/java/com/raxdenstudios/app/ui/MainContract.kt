package com.raxdenstudios.app.ui

import com.raxdenstudios.app.ui.model.BottomBarItemModel
import com.raxdenstudios.app.core.navigation.NavigationCommand
import java.util.UUID

/**
 * https://developer.android.com/topic/architecture/ui-layer
 * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
 * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
 */
object MainContract {

    data class UIState(
        val items: List<BottomBarItemModel> = BottomBarItemModel.default,
        val events: Set<UIEvent> = emptySet(),
    )

    sealed class UserEvent {

        data class BottomBarItemSelected(val item: BottomBarItemModel) : UserEvent()
    }

    sealed class UIEvent {

        val id: String = UUID.randomUUID().toString()

        data class NavigateTo(val command: NavigationCommand) : UIEvent()
    }
}
