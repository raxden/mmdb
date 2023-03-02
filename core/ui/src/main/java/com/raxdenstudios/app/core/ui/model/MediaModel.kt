package com.raxdenstudios.app.core.ui.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType

data class MediaModel(
    val id: MediaId,
    val mediaType: MediaType,
    val title: String,
    val overview: String,
    val backdrop: String,
    val poster: String,
    val rating: String,
    val releaseDate: String,
    val duration: String,
    val certification: String,
    val genres: String,
    val watchlist: Boolean,
) {

    companion object {

        val empty = MediaModel(
            id = MediaId(0L),
            mediaType = MediaType.Movie,
            title = "",
            overview = "",
            backdrop = "",
            poster = "",
            rating = "0.0",
            releaseDate = "",
            duration = "",
            certification = "",
            genres = "",
            watchlist = false,
        )

        @VisibleForTesting
        val mock = MediaModel(
            id = MediaId(1L),
            mediaType = MediaType.Movie,
            title = "The Last of Us",
            overview = "Twenty years after modern civilization has been destroyed...",
            backdrop = "",
            poster = "",
            rating = "0.0",
            releaseDate = "1970",
            duration = "",
            certification = "PEGI 18",
            genres = "Drama, Sci-Fi & Fantasy",
            watchlist = false,
        )
    }
}
