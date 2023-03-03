package com.raxdenstudios.app.core.model

import androidx.annotation.VisibleForTesting
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import java.util.Locale

sealed class Media {

    abstract val id: MediaId
    abstract val overview: String
    abstract val backdrop: Picture
    abstract val poster: Picture
    abstract val vote: Vote
    abstract val watchList: Boolean
    abstract val genres: List<Genre>
    abstract val duration: Duration
    abstract val certification: String
    abstract val originalLanguage: Locale
    abstract val spokenLanguages: List<Locale>

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
        override val genres: List<Genre>,
        override val duration: Duration,
        override val certification: String = "",
        override val originalLanguage: Locale,
        override val spokenLanguages: List<Locale>,
        val budget: Double,
        val revenue: Double,
        val title: String,
        val release: LocalDate,
    ) : Media() {

        companion object {

            @VisibleForTesting
            val mock = Movie(
                id = MediaId(1L),
                title = "The Last of Us",
                overview = "Twenty years after modern civilization has been destroyed...",
                backdrop = Picture.WithImage(
                    thumbnail = Size.Thumbnail(
                        url = "https://image.tmdb.org/t/p/w500/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg"
                    ),
                    original = Size.Original(
                        url = "https://image.tmdb.org/t/p/original/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg"
                    )
                ),
                poster = Picture.Empty,
                genres = listOf(
                    Genre(GenreId(1L), "Drama"),
                    Genre(GenreId(2L), "Sci-Fi & Fantasy"),
                ),
                duration = Duration.ofMinutes(0),
                release = LocalDate.of(1970, 1, 1),
                originalLanguage = Locale("en"),
                spokenLanguages = emptyList(),
                vote = Vote.empty,
                watchList = false,
                certification = "PEGI 18",
                budget = 0.0,
                revenue = 0.0,
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
        override val genres: List<Genre>,
        override val duration: Duration,
        override val certification: String = "",
        override val originalLanguage: Locale,
        override val spokenLanguages: List<Locale>,
        val name: String,
        val firstAirDate: LocalDate,
    ) : Media() {

        companion object {

            @VisibleForTesting
            val mock = TVShow(
                id = MediaId(1L),
                name = "The Last of Us",
                overview = "Twenty years after modern civilization has been destroyed...",
                backdrop = Picture.WithImage(
                    thumbnail = Size.Thumbnail(
                        url = "https://image.tmdb.org/t/p/w500/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg"
                    ),
                    original = Size.Original(
                        url = "https://image.tmdb.org/t/p/original/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg"
                    )
                ),
                poster = Picture.Empty,
                genres = listOf(
                    Genre(GenreId(1L), "Drama"),
                    Genre(GenreId(2L), "Sci-Fi & Fantasy"),
                ),
                duration = Duration.ofMinutes(0),
                firstAirDate = LocalDate.of(1970, 1, 1),
                originalLanguage = Locale("en"),
                spokenLanguages = emptyList(),
                vote = Vote.empty,
                watchList = false,
                certification = "PEGI 18",
            )
        }
    }
}
