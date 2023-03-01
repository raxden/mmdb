package com.raxdenstudios.app.core.network.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class VideoDto(
    @Expose val iso_639_1: String,
    @Expose val iso_3166_1: String,
    @Expose val name: String,
    @Expose val key: String,
    @Expose val site: String,
    @Expose val size: Int,
    @Expose val type: String,
    @Expose val official: Boolean,
    @Expose val published_at: String,
    @Expose val id: String,
) {

    companion object {

        @VisibleForTesting
        val mock = VideoDto(
            iso_639_1 = "",
            iso_3166_1 = "",
            name = "The Last of Us",
            key = "l6rAoph5UgI",
            site = "YouTube",
            size = 0,
            type = "Trailer",
            official = false,
            published_at = "",
            id = "",
        )
    }
}
