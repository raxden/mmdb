package com.raxdenstudios.app.list.view.model

import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class MediaListModel(
  val title: String,
  val items: List<MediaListItemModel>,
) {

  fun replaceMovie(mediaListItemModel: MediaListItemModel): MediaListModel = this.copy(
    items = this.items.replaceItem(mediaListItemModel) { it.id == mediaListItemModel.id }
  )

  companion object {
    val empty = MediaListModel(
      title = "",
      items = emptyList()
    )
  }
}
