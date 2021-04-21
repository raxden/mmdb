package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class CarouselMediaListModel(
  val label: String,
  val description: String,
  val medias: List<MediaListItemModel>,
) {

  fun hasMovies() = medias.isNotEmpty()

  fun replaceMovie(media: MediaListItemModel): CarouselMediaListModel = copy(
    medias = medias.replaceItem(media) { mediaToReplace -> mediaToReplace.id == media.id }
  )

  companion object {
    @VisibleForTesting
    val empty = CarouselMediaListModel(
      label = "",
      description = "",
      medias = emptyList(),
    )
  }
}
