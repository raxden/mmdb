package com.raxdenstudios.app.feature

import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.pagination.model.PageIndex
import java.util.UUID

object MediaListContract {

    data class UIState(
        val isLoading: Boolean = false,
        val isLoadingMoreContent: Boolean = false,
        val title: String,
        val items: List<MediaModel>,
        val events: Set<UIEvent> = emptySet(),
        val error: ErrorModel? = null,
    ) {

        companion object {

            val empty = UIState(
                title = "",
                items = emptyList()
            )

            fun loading() = UIState(
                isLoading = true,
                isLoadingMoreContent = false,
                title = "",
                items = emptyList(),
                error = null,
            )
        }
    }

    sealed interface UserEvent {
        object ErrorDismissed : UserEvent
        object Refresh : UserEvent
        data class LoadMore(val pageIndex: PageIndex) : UserEvent
        data class WatchButtonClicked(val item: MediaModel) : UserEvent
        data class MediaClicked(val item: MediaModel) : UserEvent
        object BackClicked : UserEvent
    }

    /**
     * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
     * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
     */
    sealed class UIEvent {
        val id: String = UUID.randomUUID().toString()

        object NavigateToBack : UIEvent()
        data class NavigateToMedia(val mediaId: MediaId, val mediaType: MediaType) : UIEvent()
    }
}
