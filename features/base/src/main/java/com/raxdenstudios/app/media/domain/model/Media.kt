package com.raxdenstudios.app.media.domain.model

import androidx.annotation.VisibleForTesting
import org.threeten.bp.LocalDate

sealed class Media {

  abstract val id: Long
  abstract val backdrop: Picture
  abstract val poster: Picture
  abstract val vote: Vote
  abstract val watchList: Boolean

  fun copyWith(watchList: Boolean): Media = when (this) {
    is Movie -> copy(watchList = watchList)
    is TVShow -> copy(watchList = watchList)
  }

  data class Movie(
    override val id: Long,
    override val backdrop: Picture,
    override val poster: Picture,
    override val vote: Vote,
    override val watchList: Boolean,
    val title: String,
    val release: LocalDate,
  ) : Media() {

    companion object {
      fun withId(id: Long) = Movie(
        id = id,
        title = "",
        backdrop = Picture.Empty,
        poster = Picture.Empty,
        release = LocalDate.of(1970, 1, 1),
        vote = Vote.empty,
        watchList = false,
      )

      @VisibleForTesting
      val empty = Movie(
        id = 0L,
        title = "",
        backdrop = Picture.Empty,
        poster = Picture.Empty,
        release = LocalDate.of(1970, 1, 1),
        vote = Vote.empty,
        watchList = false,
      )
    }
  }

  data class TVShow(
    override val id: Long,
    override val backdrop: Picture,
    override val poster: Picture,
    override val vote: Vote,
    override val watchList: Boolean,
    val name: String,
    val firstAirDate: LocalDate,
  ) : Media()
}
