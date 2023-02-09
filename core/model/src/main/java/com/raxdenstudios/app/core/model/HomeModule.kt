package com.raxdenstudios.app.core.model

sealed class HomeModule {

    abstract val id: Long
    abstract val order: Int

    data class Carousel(
        override val id: Long,
        override val order: Int,
        val mediaType: MediaType,
        val mediaCategory: MediaCategory,
        val medias: List<Media> = emptyList(),
    ) : HomeModule() {

        companion object {

            fun nowPlaying(id: Long, order: Int, medias: List<Media> = emptyList()) = Carousel(
                id = id,
                order = order,
                mediaType = MediaType.Movie,
                mediaCategory = MediaCategory.NowPlaying,
                medias = medias,
            )

            fun upcoming(id: Long, order: Int, medias: List<Media> = emptyList()) = Carousel(
                id = id,
                order = order,
                mediaType = MediaType.Movie,
                mediaCategory = MediaCategory.Upcoming,
                medias = medias,
            )

            fun popular(id: Long, order: Int, mediaType: MediaType, medias: List<Media> = emptyList()) = Carousel(
                id = id,
                order = order,
                mediaType = mediaType,
                mediaCategory = MediaCategory.Popular,
                medias = medias,
            )

            fun watchlist(id: Long, order: Int, mediaType: MediaType, medias: List<Media> = emptyList()) = Carousel(
                id = id,
                order = order,
                mediaType = mediaType,
                mediaCategory = MediaCategory.Watchlist,
                medias = medias,
            )

            fun topRated(id: Long, order: Int, mediaType: MediaType, medias: List<Media> = emptyList()) = Carousel(
                id = id,
                order = order,
                mediaType = mediaType,
                mediaCategory = MediaCategory.TopRated,
                medias = medias,
            )
        }
    }

    data class OtherModule(
        override val id: Long,
        override val order: Int,
    ) : HomeModule()
}
