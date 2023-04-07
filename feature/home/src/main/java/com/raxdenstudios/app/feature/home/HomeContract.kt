package com.raxdenstudios.app.feature.home

import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
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
        val error: ErrorModel? = null,
    ) {

        companion object {

            val loading = UIState(
                isLoading = true,
                modules = emptyList(),
                error = null,
            )
        }
    }

    sealed interface UserEvent {

        object ErrorDismissed : UserEvent
        data class MediaSelected(
            val item: MediaModel,
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

        data class NavigateToMedias(
            val mediaType: MediaType,
            val mediaCategory: MediaCategory,
        ) : UIEvent()

        data class NavigateToMedia(
            val mediaId: MediaId,
            val mediaType: MediaType,
        ) : UIEvent()

        data class ShowMessage(val message: String) : UIEvent()
    }
}
