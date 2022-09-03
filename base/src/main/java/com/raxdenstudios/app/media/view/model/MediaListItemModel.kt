package com.raxdenstudios.app.media.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType

data class MediaListItemModel(
    val id: MediaId,
    val mediaType: MediaType,
    val title: String,
    val backdrop: String,
    val image: String,
    val rating: String,
    val releaseDate: String,
    val watchButtonModel: WatchButtonModel,
) {

    companion object {
        @VisibleForTesting
        val empty = MediaListItemModel(
            id = MediaId(0L),
            mediaType = MediaType.MOVIE,
            title = "",
            backdrop = "",
            image = "",
            rating = "0.0",
            releaseDate = "1970",
            watchButtonModel = WatchButtonModel.Unselected
        )
    }
}
