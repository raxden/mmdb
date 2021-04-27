package com.raxdenstudios.app.list.view.model

import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class MediaListModel(
  val logged: Boolean,
  val media: List<MediaListItemModel>,
) {

  fun replaceMovie(mediaListItemModel: MediaListItemModel): MediaListModel = this.copy(
    media = this.media.replaceItem(mediaListItemModel) { it.id == mediaListItemModel.id }
  )

  companion object {
    val empty = MediaListModel(
      logged = false,
      media = emptyList()
    )
  }
}