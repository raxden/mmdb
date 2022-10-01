package com.raxdenstudios.app.feature.home

import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import java.util.UUID

/**
 * https://developer.android.com/topic/architecture/ui-layer
 * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
 * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
 */
object HomeContract {

    data class UIState(
        val isLoading: Boolean = false,
        val modules: List<HomeModuleModel>,
        val events: Set<UIEvent> = emptySet(),
        val error: Throwable? = null,
    ) {

        companion object {

            fun loading() = UIState(
                isLoading = true,
                modules = emptyList(),
                events = emptySet(),
                error = null,
            )
        }
    }

    sealed interface UserEvent {

        data class MediaSelected(
            val mediaItemModel: MediaModel,
        ) : UserEvent

        data class WatchButtonClicked(
            val item: MediaModel,
        ) : UserEvent

        data class SeeAllButtonClicked(val module: HomeModuleModel.Carousel) : UserEvent
        data class MediaFilterClicked(
            val module: HomeModuleModel.Carousel,
            val filter: MediaFilterModel,
        ) : UserEvent
    }

    sealed class UIEvent {

        val id: String = UUID.randomUUID().toString()

        data class NavigateToMediaList(
            val mediaType: Int,
            val mediaCategory: Int,
        ) : UIEvent()
    }
}
