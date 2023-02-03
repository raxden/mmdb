package com.raxdenstudios.app.feature.detail

import com.raxdenstudios.app.core.ui.model.MediaModel
import java.util.UUID

object MediaContract {

    data class UIState(
        val isLoading: Boolean = false,
        val media: MediaModel = MediaModel.empty,
        val events: Set<UIEvent> = emptySet(),
        val error: Throwable? = null,
    ) {

        companion object {
            val loading = UIState(
                isLoading = true
            )
        }
    }

    sealed interface UserEvent {
        object BackClicked : UserEvent
        data class AddToWatchlist(val media: MediaModel) : UserEvent
        data class RemoveFromWatchlist(val media: MediaModel) : UserEvent
    }

    /**
     * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
     * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
     */
    sealed class UIEvent {
        val id: String = UUID.randomUUID().toString()

        object NavigateToBack : UIEvent()
    }
}
