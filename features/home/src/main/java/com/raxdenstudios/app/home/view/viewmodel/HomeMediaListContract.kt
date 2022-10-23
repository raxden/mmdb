package com.raxdenstudios.app.home.view.viewmodel

import com.raxdenstudios.app.home.view.model.HomeMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import java.util.UUID

internal class HomeMediaListContract {

    internal data class UIState(
        val isLoading: Boolean = false,
        val model: HomeMediaListModel,
        val events: Set<UIEvent> = emptySet(),
        val error: Throwable? = null,
    ) {

        companion object {

            fun loading() = UIState(
                isLoading = true,
                model = HomeMediaListModel.empty,
                events = emptySet(),
                error = null,
            )
        }
    }

    internal sealed class UserEvent {
        data class MediaSelected(
            val module: HomeModuleModel.CarouselMedias,
            val mediaItemModel: MediaListItemModel,
        ) : UserEvent()

        data class WatchButtonClicked(val item: MediaListItemModel) : UserEvent()
        data class ViewAllButtonClicked(val module: HomeModuleModel.CarouselMedias) : UserEvent()
        data class FilterChanged(
            val module: HomeModuleModel.CarouselMedias,
            val mediaType: MediaType,
        ) : UserEvent()
    }

    /**
     * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
     * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
     */
    internal sealed class UIEvent {

        val id: String = UUID.randomUUID().toString()

        data class NavigateToMediaList(
            val carouselMedias: HomeModuleModel.CarouselMedias,
        ) : UIEvent()
    }
}
