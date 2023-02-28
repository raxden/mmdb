package com.raxdenstudios.app.core.model

import org.threeten.bp.LocalDate

data class Video(
    val uri: String,
    val name: String,
    val site: String,
    val size: Int,
    val official: Boolean,
    val type: VideoType,
    val published: LocalDate,
    val picture: Picture,
) {

    companion object {

        val mock = Video(
            uri = "https://www.youtube.com/watch?v=l6rAoph5UgI",
            name = "The Last of Us",
            site = "",
            size = 0,
            official = false,
            type = VideoType.Trailer,
            published = LocalDate.of(1970, 1, 1),
            picture = Picture.Empty,
        )
    }
}
