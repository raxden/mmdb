package com.raxdenstudios.app.core.model

@Suppress("MagicNumber")
sealed class MediaCategory {

    abstract val value: Int

    object NowPlaying : MediaCategory() {

        override val value: Int = 1
    }

    object Upcoming : MediaCategory() {

        override val value: Int = 2
    }

    object Popular : MediaCategory() {

        override val value: Int = 3
    }

    object TopRated : MediaCategory() {

        override val value: Int = 4
    }

    object Watchlist : MediaCategory() {

        override val value: Int = 5
    }

    companion object {

        fun fromValue(value: Int): MediaCategory {
            return when (value) {
                NowPlaying.value -> NowPlaying
                Upcoming.value -> Upcoming
                Popular.value -> Popular
                TopRated.value -> TopRated
                Watchlist.value -> Watchlist
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}
