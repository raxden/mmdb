package com.raxdenstudios.app.list.view.model

import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class MediaListModel(
  val mediaFilterModel: MediaFilterModel,
  val logged: Boolean,
  val media: List<MediaListItemModel>,
) {

  fun replaceMovie(mediaListItemModel: MediaListItemModel): MediaListModel = this.copy(
    media = this.media.replaceItem(mediaListItemModel) { it.id == mediaListItemModel.id }
  )

  companion object {
    val empty = MediaListModel(
      logged = false,
      mediaFilterModel = MediaFilterModel.Popular.popularMoviesMediaFilter,
      media = emptyList()
    )

    fun withFilter(mediaFilterModel: MediaFilterModel) = MediaListModel(
      logged = false,
      mediaFilterModel = mediaFilterModel,
      media = emptyList()
    )
  }
}