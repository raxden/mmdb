package com.raxdenstudios.app.core.model

data class MediaFilter(
    val mediaType: MediaType,
    val mediaCategory: MediaCategory,
) {

    companion object {

        val nowPlaying = MediaFilter(
            mediaType = MediaType.Movie,
            mediaCategory = MediaCategory.NowPlaying,
        )
        val upcoming = MediaFilter(
            mediaType = MediaType.Movie,
            mediaCategory = MediaCategory.Upcoming,
        )
        fun popular(mediaType: MediaType) = MediaFilter(
            mediaType = mediaType,
            mediaCategory = MediaCategory.Popular,
        )
        fun topRated(mediaType: MediaType) = MediaFilter(
            mediaType = mediaType,
            mediaCategory = MediaCategory.TopRated,
        )
        fun wachlist(mediaType: MediaType) = MediaFilter(
            mediaType = mediaType,
            mediaCategory = MediaCategory.Watchlist,
        )
    }
}
