package com.raxdenstudios.app.core.network.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming", "VariableNaming")
sealed class MediaDetailDto {

    abstract val backdrop_path: String?
    abstract val genres: List<GenreDto>
    abstract val id: Int
    abstract val original_language: String
    abstract val spoken_languages: List<SpokenLanguageDto>
    abstract val overview: String
    abstract val popularity: Double
    abstract val poster_path: String
    abstract val vote_average: Double
    abstract val vote_count: Int
    abstract val homepage: String
    abstract val adult: Boolean
    abstract val status: String
    abstract val tagline: String

    data class Movie(
        @Expose override val backdrop_path: String?,
        @Expose override val genres: List<GenreDto>,
        @Expose override val id: Int,
        @Expose override val original_language: String,
        @Expose override val spoken_languages: List<SpokenLanguageDto>,
        @Expose override val overview: String,
        @Expose override val popularity: Double,
        @Expose override val poster_path: String,
        @Expose override val vote_average: Double,
        @Expose override val vote_count: Int,
        @Expose override val homepage: String,
        @Expose override val adult: Boolean,
        @Expose override val status: String,
        @Expose override val tagline: String,
        @Expose val budget: Double,
        @Expose val revenue: Double,
        @Expose val runtime: Int,
        @Expose val title: String,
        @Expose val original_title: String,
        @Expose val video: Boolean,
        @Expose val release_date: String,
        @Expose val release_dates: ResultsDto<RegionReleaseDateDto>
    ) : MediaDetailDto() {

        @VisibleForTesting
        constructor() : this(
            adult = false,
            backdrop_path = "",
            budget = 0.0,
            genres = emptyList(),
            homepage = "",
            id = 0,
            original_language = "",
            spoken_languages = emptyList(),
            original_title = "",
            overview = "",
            popularity = 0.0,
            poster_path = "",
            release_date = "1970-01-01",
            revenue = 0.0,
            runtime = 0,
            status = "",
            tagline = "",
            title = "",
            video = false,
            vote_average = 0.0,
            vote_count = 0,
            release_dates = ResultsDto(emptyList()),
        )

        companion object {

            @VisibleForTesting
            val mock = Movie(
                adult = false,
                backdrop_path = "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg",
                budget = 0.0,
                genres = emptyList(),
                homepage = "",
                id = 1,
                original_language = "en",
                spoken_languages = emptyList(),
                original_title = "",
                overview = "Twenty years after modern civilization has been destroyed...",
                popularity = 0.0,
                poster_path = "",
                release_date = "1970-01-01",
                runtime = 0,
                revenue = 0.0,
                status = "",
                tagline = "",
                title = "The Last of Us",
                video = false,
                vote_average = 0.0,
                vote_count = 0,
                release_dates = ResultsDto(
                    listOf(RegionReleaseDateDto.mock)
                ),
            )
        }
    }

    data class TVShow(
        @Expose override val backdrop_path: String?,
        @Expose override val genres: List<GenreDto>,
        @Expose override val id: Int,
        @Expose override val original_language: String,
        @Expose override val spoken_languages: List<SpokenLanguageDto>,
        @Expose override val overview: String,
        @Expose override val popularity: Double,
        @Expose override val poster_path: String,
        @Expose override val vote_average: Double,
        @Expose override val vote_count: Int,
        @Expose override val homepage: String,
        @Expose override val adult: Boolean,
        @Expose override val status: String,
        @Expose override val tagline: String,
        @Expose val name: String,
        @Expose val episode_run_time: List<Int>,
        @Expose val original_name: String,
        @Expose val origin_country: List<String>,
        @Expose val first_air_date: String,
        @Expose val content_ratings: ResultsDto<ContentRatingDto>,
    ) : MediaDetailDto()
}
