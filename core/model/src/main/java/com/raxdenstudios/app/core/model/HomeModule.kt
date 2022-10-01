package com.raxdenstudios.app.core.model

sealed class HomeModule {

    abstract val id: Long
    abstract val order: Int

    fun copyValue(mediaType: MediaType): HomeModule = when (this) {
        is NowPlaying -> copy(mediaType = mediaType)
        is Popular -> copy(mediaType = mediaType)
        is TopRated -> copy(mediaType = mediaType)
        is Upcoming -> copy(mediaType = mediaType)
        is Watchlist -> copy(mediaType = mediaType)
    }

    data class Popular(
        val mediaType: MediaType,
        override val id: Long,
        override val order: Int,
    ) : HomeModule() {

        companion object {

            val empty = Popular(
                mediaType = MediaType.Movie,
                id = 0L,
                order = 0,
            )
        }
    }

    data class NowPlaying(
        val mediaType: MediaType,
        override val id: Long,
        override val order: Int,
    ) : HomeModule() {

        companion object {

            val empty = NowPlaying(
                mediaType = MediaType.Movie,
                id = 0L,
                order = 0,
            )
        }
    }

    data class TopRated(
        val mediaType: MediaType,
        override val id: Long,
        override val order: Int,
    ) : HomeModule() {

        companion object {

            val empty = TopRated(
                mediaType = MediaType.Movie,
                id = 0L,
                order = 0,
            )
        }
    }

    data class Upcoming(
        val mediaType: MediaType,
        override val id: Long,
        override val order: Int,
    ) : HomeModule() {


        companion object {

            val empty = Upcoming(
                mediaType = MediaType.Movie,
                id = 0L,
                order = 0,
            )
        }
    }

    data class Watchlist(
        val mediaType: MediaType,
        override val id: Long,
        override val order: Int,
    ) : HomeModule() {


        companion object {

            val empty = Watchlist(
                mediaType = MediaType.Movie,
                id = 0L,
                order = 0,
            )
        }
    }
}
