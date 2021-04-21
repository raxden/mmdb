package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class CarouselMediaListModel(
  val label: String,
  val description: String,
  val medias: List<MediaListItemModel>,
) {

  fun hasMedias() = medias.isNotEmpty()

  fun replaceMedia(media: MediaListItemModel): CarouselMediaListModel = copy(
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
