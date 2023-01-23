package com.raxdenstudios.app.feature.component

import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.ui.model.MediaModel

object ListPreviewData {

    @SuppressWarnings("MagicNumber")
    val medias = List(10) {
        MediaModel.empty.copy(
            id = MediaId(it.toLong()),
            title = "The Batman",
            releaseDate = "2011",
            rating = "7.8",
            poster = "https://image.tmdb.org/t/p/w500/8Y43POKjjKDGI9MH89NW0NAzzp8.jpg"
        )
    }
}
