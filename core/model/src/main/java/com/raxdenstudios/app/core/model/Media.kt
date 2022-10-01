package com.raxdenstudios.app.core.model

import org.threeten.bp.LocalDate

sealed class Media {

    abstract val id: MediaId
    abstract val overview: String
    abstract val backdrop: Picture
    abstract val poster: Picture
    abstract val vote: Vote
    abstract val watchList: Boolean

    fun copyWith(watchList: Boolean): Media = when (this) {
        is Movie -> copy(watchList = watchList)
        is TVShow -> copy(watchList = watchList)
    }

    data class Movie(
        override val id: MediaId,
        override val overview: String,
        override val backdrop: Picture,
        override val poster: Picture,
        override val vote: Vote,
        override val watchList: Boolean,
        val title: String,
        val release: LocalDate,
    ) : Media() {

        companion object {

            @Suppress("MagicNumber")
            fun withId(id: MediaId) = Movie(
                id = id,
                title = "",
                overview = "",
                backdrop = Picture.Empty,
                poster = Picture.Empty,
                release = LocalDate.of(1970, 1, 1),
                vote = Vote.empty,
                watchList = false,
            )

            @Suppress("MagicNumber")
            val empty = Movie(
                id = MediaId(0L),
                title = "",
                overview = "",
                backdrop = Picture.Empty,
                poster = Picture.Empty,
                release = LocalDate.of(1970, 1, 1),
                vote = Vote.empty,
                watchList = false,
            )
        }
    }

    data class TVShow(
        override val id: MediaId,
        override val overview: String,
        override val backdrop: Picture,
        override val poster: Picture,
        override val vote: Vote,
        override val watchList: Boolean,
        val name: String,
        val firstAirDate: LocalDate,
    ) : Media()
}
