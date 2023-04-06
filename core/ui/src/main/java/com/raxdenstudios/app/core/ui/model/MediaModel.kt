package com.raxdenstudios.app.core.ui.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType

data class MediaModel(
    val id: MediaId,
    val mediaType: MediaType,
    val title: String,
    val overview: String,
    val backdrop: String,
    val poster: String,
    val rating: String,
    val releaseYear: String,
    val releaseDate: String,
    val duration: String,
    val certification: String,
    val genres: String,
    val watchlist: Boolean,
    val originalLanguage: String,
    val spokenLanguages: String,
    val budget: String,
    val revenue: String,
) {

    companion object {

        val empty = MediaModel(
            id = MediaId(0L),
            mediaType = MediaType.Movie,
            title = "",
            overview = "",
            backdrop = "",
            poster = "",
            rating = "0",
            releaseYear = "",
            releaseDate = "",
            duration = "",
            certification = "",
            genres = "",
            watchlist = false,
            originalLanguage = "",
            spokenLanguages = "",
            budget = "",
            revenue = "",
        )

        @VisibleForTesting
        val mock = MediaModel(
            id = MediaId(1L),
            mediaType = MediaType.Movie,
            title = "The Last of Us",
            overview = "Twenty years after modern civilization has been destroyed...",
            backdrop = "https://image.tmdb.org/t/p/w500/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg",
            poster = "",
            rating = "0",
            releaseYear = "1970",
            releaseDate = "January 1, 1970",
            duration = "",
            certification = "PEGI 18",
            genres = "Drama, Sci-Fi & Fantasy",
            watchlist = false,
            originalLanguage = "English",
            spokenLanguages = "",
            budget = "",
            revenue = "",
        )
    }
}
