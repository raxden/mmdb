package com.raxdenstudios.app.list.view.model

import android.os.Parcelable
import com.raxdenstudios.app.media.domain.model.MediaType
import kotlinx.parcelize.Parcelize

sealed class MediaListParams(
  open val mediaType: MediaType
) : Parcelable {

  @Parcelize
  data class Popular(
    override val mediaType: MediaType
  ) : MediaListParams(mediaType)

  @Parcelize
  data class WatchList(
    override val mediaType: MediaType
  ) : MediaListParams(mediaType)

  @Parcelize
  data class TopRated(
    override val mediaType: MediaType
  ) : MediaListParams(mediaType)

  @Parcelize
  object NowPlaying : MediaListParams(MediaType.MOVIE)

  @Parcelize
  object Upcoming : MediaListParams(MediaType.MOVIE)

  companion object {
    val popularMovies = Popular(
      mediaType = MediaType.MOVIE
    )
  }
}
