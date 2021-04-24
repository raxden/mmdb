package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.media.view.model.MediaFilterModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.removeItem
import com.raxdenstudios.commons.ext.replaceItem

data class CarouselMediaListModel(
  val label: String,
  val description: String,
  val mediaFilterModel: MediaFilterModel,
  val medias: List<MediaListItemModel>,
) {

  fun hasMedias() = medias.isNotEmpty()

  fun updateMedia(media: MediaListItemModel): CarouselMediaListModel =
    updateMedia(media, mediaFilterModel)

  private fun updateMedia(
    media: MediaListItemModel,
    mediaFilterModel: MediaFilterModel
  ): CarouselMediaListModel = when (mediaFilterModel) {
    is MediaFilterModel.NowPlaying,
    is MediaFilterModel.Popular,
    is MediaFilterModel.TopRated,
    MediaFilterModel.Upcoming -> replaceItem(media)
    is MediaFilterModel.WatchList -> when (media.watchButtonModel) {
      WatchButtonModel.Selected -> addMedia(media)
      WatchButtonModel.Unselected -> removeMedia(media)
    }
  }

  private fun addMedia(media: MediaListItemModel): CarouselMediaListModel = when {
    medias.contains(media) -> this
    else -> copy(medias = medias.toMutableList().apply { add(0, media) }.toList())
  }

  private fun removeMedia(media: MediaListItemModel): CarouselMediaListModel = copy(
    medias = medias.removeItem { mediaToRemove -> mediaToRemove.id == media.id }
  )

  private fun replaceItem(media: MediaListItemModel): CarouselMediaListModel = copy(
    medias = medias.replaceItem(media) { mediaToReplace -> mediaToReplace.id == media.id }
  )

  companion object {
    @VisibleForTesting
    val empty = CarouselMediaListModel(
      label = "",
      description = "",
      mediaFilterModel = MediaFilterModel.popularMovies,
      medias = emptyList(),
    )
  }
}
