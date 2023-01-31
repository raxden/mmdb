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
    val contentRating: String,
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
            releaseDate = "1970",
            duration = "",
            contentRating = "",
            genres = "",
            watchlist = false,
        )

        @VisibleForTesting
        val mock = MediaModel(
            id = MediaId(1L),
            mediaType = MediaType.Movie,
            title = "The Last of Us",
            overview = "Twenty years after modern civilization has been destroyed, Joel, a hardened survivor, is " +
                "hired to smuggle Ellie, a 14-year-old girl, out of an oppressive quarantine zone. What starts as " +
                "a small job soon becomes a brutal, heartbreaking journey, as they both must traverse the United " +
                "States and depend on each other for survival.",
            backdrop = "https://image.tmdb.org/t/p/w500/uDgy6hyPd82kOHh6I95FLtLnj6p.jpg",
            poster = "https://image.tmdb.org/t/p/w500/uKvVjHNqB5VmOrdxqAt2F7J78ED.jpg",
            rating = "8.0",
            releaseDate = "2023",
            duration = "1 hour",
            contentRating = "TV-MA",
            genres = "Drama, Sci-Fi & Fantasy, Action & Adventure",
            watchlist = false,
        )
    }
}
