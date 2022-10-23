package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.media.view.model.MediaListItemModel

data class HomeMediaListModel(
    val modules: List<HomeModuleModel>,
) {

    fun updateMedia(media: MediaListItemModel): HomeMediaListModel = copy(
        modules = modules.map { module ->
            when (module) {
                is HomeModuleModel.CarouselMedias -> module.updateMedia(media)
            }
        }
    )

    companion object {

        @VisibleForTesting
        val empty = HomeMediaListModel(
            modules = emptyList()
        )
    }
}
