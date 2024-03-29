package com.raxdenstudios.app.core.network.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming", "VariableNaming")
sealed class MediaDto {

    abstract val backdrop_path: String?
    abstract val genre_ids: List<Int>
    abstract val id: Int
    abstract val original_language: String
    abstract val overview: String
    abstract val popularity: Double
    abstract val poster_path: String
    abstract val vote_average: Double
    abstract val vote_count: Int

    data class Movie(
        @Expose override val backdrop_path: String?,
        @Expose override val genre_ids: List<Int>,
        @Expose override val id: Int,
        @Expose override val original_language: String,
        @Expose override val overview: String,
        @Expose override val popularity: Double,
        @Expose override val poster_path: String,
        @Expose override val vote_average: Double,
        @Expose override val vote_count: Int,
        @Expose val adult: Boolean,
        @Expose val title: String,
        @Expose val original_title: String,
        @Expose val video: Boolean,
        @Expose val release_date: String,
    ) : MediaDto() {

        @VisibleForTesting
        constructor() : this(
            adult = false,
            backdrop_path = "",
            genre_ids = emptyList(),
            id = 0,
            original_language = "",
            original_title = "",
            overview = "",
            popularity = 0.0,
            poster_path = "",
            release_date = "1970-01-01",
            title = "",
            video = false,
            vote_average = 0.0,
            vote_count = 0,
        )

        companion object {

            @VisibleForTesting
            val mock = Movie(
                adult = false,
                backdrop_path = "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg",
                genre_ids = emptyList(),
                id = 1,
                original_language = "en",
                original_title = "",
                overview = "Twenty years after modern civilization has been destroyed...",
                popularity = 0.0,
                poster_path = "",
                release_date = "1970-01-01",
                title = "The Last of Us",
                video = false,
                vote_average = 0.0,
                vote_count = 0,
            )
        }
    }

    data class TVShow(
        @Expose override val backdrop_path: String?,
        @Expose override val genre_ids: List<Int>,
        @Expose override val id: Int,
        @Expose override val original_language: String,
        @Expose override val overview: String,
        @Expose override val popularity: Double,
        @Expose override val poster_path: String,
        @Expose override val vote_average: Double,
        @Expose override val vote_count: Int,
        @Expose val name: String,
        @Expose val original_name: String,
        @Expose val origin_country: List<String>,
        @Expose val first_air_date: String,
    ) : MediaDto() {

        companion object {

            @VisibleForTesting
            val mock = TVShow(
                backdrop_path = "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg",
                genre_ids = emptyList(),
                id = 1,
                original_language = "en",
                original_name = "",
                overview = "Twenty years after modern civilization has been destroyed...",
                popularity = 0.0,
                poster_path = "",
                first_air_date = "1970-01-01",
                name = "The Last of Us",
                origin_country = emptyList(),
                vote_average = 0.0,
                vote_count = 0,
            )
        }
    }
}
