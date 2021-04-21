package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class CarouselMovieListModel(
  val label: String,
  val description: String,
  val medias: List<MediaListItemModel>,
) {

  fun hasMovies() = medias.isNotEmpty()

  fun replaceMovie(media: MediaListItemModel): CarouselMovieListModel = copy(
    medias = medias.replaceItem(media) { movieToReplace -> movieToReplace.id == media.id }
  )

  companion object {
    @VisibleForTesting
    val empty = CarouselMovieListModel(
      label = "",
      description = "",
      medias = emptyList(),
    )
  }
}
