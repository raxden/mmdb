package com.raxdenstudios.app.feature.detail

import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.detail.model.RelatedMediasModel
import com.raxdenstudios.app.feature.detail.model.VideoModel
import java.util.UUID

object MediaContract {

    data class UIState(
        val isLoading: Boolean = false,
        val media: MediaModel = MediaModel.empty,
        val videos: List<VideoModel> = emptyList(),
        val relatedMedias: RelatedMediasModel = RelatedMediasModel.empty,
        val events: Set<UIEvent> = emptySet(),
        val error: ErrorModel? = null,
    ) {

        companion object {
            val loading = UIState(
                isLoading = true
            )
        }
    }

    sealed interface UserEvent {
        object BackClicked : UserEvent
        object ErrorDismissed : UserEvent
        data class VideoClick(val video: VideoModel) : UserEvent
        data class WatchlistClick(val media: MediaModel) : UserEvent
        data class RelatedMediaClick(val media: MediaModel) : UserEvent
        data class RelatedWatchlistClick(val media: MediaModel) : UserEvent
        object RelatedSeeAllButtonClicked : UserEvent
    }

    /**
     * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
     * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
     */
    sealed class UIEvent {
        val id: String = UUID.randomUUID().toString()

        data class PlayYoutubeVideo(val url: String) : UIEvent()
        object NavigateToBack : UIEvent()
        data class NavigateToMedia(
            val mediaId: MediaId,
            val mediaType: MediaType,
        ) : UIEvent()

        data class NavigateToMedias(
            val mediaType: MediaType,
            val mediaCategory: MediaCategory,
        ) : UIEvent()

        data class NavigateToRelatedMedias(
            val mediaId: MediaId,
            val mediaType: MediaType,
        ) : UIEvent()
    }
}
