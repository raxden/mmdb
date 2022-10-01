package com.raxdenstudios.app.core.model

sealed class MediaType {

    abstract val value: Int

    object Movie : MediaType() {

        override val value: Int = 1
    }

    object TvShow : MediaType() {

        override val value: Int = 2
    }

    companion object {

        fun fromValue(value: Int): MediaType {
            return when (value) {
                Movie.value -> Movie
                TvShow.value -> TvShow
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}
