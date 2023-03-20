package com.raxdenstudios.app.feature.search

import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.search.model.SearchBarModel
import java.util.UUID

object SearchContract {

    data class UIState(
        val searchBarModel: SearchBarModel = SearchBarModel.Idle,
        val results: List<MediaModel> = emptyList(),
        val events: Set<UIEvent> = emptySet(),
        val error: ErrorModel? = null,
    )

    sealed interface UserEvent {
        object ErrorDismissed : UserEvent
        data class MediaClicked(val media: MediaModel) : UserEvent
        data class SearchBarQueryChanged(val query: String) : UserEvent
        data class SearchClicked(val query: String) : UserEvent
        object ClearSearchBarClicked : UserEvent
        data class MediaWatchButtonClicked(val media: MediaModel) : UserEvent
    }

    sealed class UIEvent {

        val id: String = UUID.randomUUID().toString()

        data class NavigateToMedia(
            val mediaId: MediaId,
            val mediaType: MediaType
        ) : UIEvent()
    }
}
