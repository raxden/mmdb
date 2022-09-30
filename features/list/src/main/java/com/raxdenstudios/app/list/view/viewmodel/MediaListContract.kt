package com.raxdenstudios.app.list.view.viewmodel

import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.pagination.model.PageIndex

internal class MediaListContract {

    internal sealed class UserEvent {
        object Refresh : UserEvent()
        data class LoadMore(val pageIndex: PageIndex) : UserEvent()
        data class WatchButtonClicked(val item: MediaListItemModel) : UserEvent()
    }

    internal data class UIState(
        val isLoading: Boolean = false,
        val model: MediaListModel,
        val error: Throwable? = null,
    ) {

        companion object {

            fun loading() = UIState(
                isLoading = true,
                model = MediaListModel.empty,
                error = null,
            )
        }
    }
}
