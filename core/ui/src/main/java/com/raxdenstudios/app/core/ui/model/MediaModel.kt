package com.raxdenstudios.app.core.ui.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType

data class MediaModel(
    val id: MediaId,
    val mediaType: MediaType,
    val title: String,
    val description: String,
    val backdrop: String,
    val image: String,
    val rating: String,
    val releaseDate: String,
    val watchButton: WatchButtonModel,
) {

    companion object {

        val empty = MediaModel(
            id = MediaId(0L),
            mediaType = MediaType.Movie,
            title = "",
            description = "",
            backdrop = "",
            image = "",
            rating = "0.0",
            releaseDate = "1970",
            watchButton = WatchButtonModel.Unselected
        )

        @VisibleForTesting
        val mock = MediaModel(
            id = MediaId(1L),
            mediaType = MediaType.Movie,
            title = "title",
            description = "description",
            backdrop = "backdrop",
            image = "image1",
            rating = "8.0",
            releaseDate = "1970",
            watchButton = WatchButtonModel.Unselected
        )
    }
}
