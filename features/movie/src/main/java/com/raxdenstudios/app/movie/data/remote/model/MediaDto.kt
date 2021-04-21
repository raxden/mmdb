package com.raxdenstudios.app.movie.data.remote.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.Expose

data class MediaDto(
  @Expose val adult: Boolean,
  @Expose val backdrop_path: String?,
  @Expose val genre_ids: List<Int>,
  @Expose val id: Int,
  @Expose val original_language: String,
  @Expose val original_title: String,
  @Expose val overview: String,
  @Expose val popularity: Double,
  @Expose val poster_path: String,
  @Expose val release_date: String,
  @Expose val title: String,
  @Expose val video: Boolean,
  @Expose val vote_average: Double,
  @Expose val vote_count: Int
) {

  companion object {
    @VisibleForTesting
    val empty = MediaDto(
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
  }
}
