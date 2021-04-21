package com.raxdenstudios.app.media.view.mapper

import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.Picture
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.util.DataMapper

class MediaListItemModelMapper : DataMapper<Media, MediaListItemModel>() {

  override fun transform(source: Media): MediaListItemModel = source.toModel()

  private fun Media.toModel() = MediaListItemModel(
    id = id,
    mediaType = mediaType,
    title = title,
    image = when (poster) {
      Picture.Empty -> ""
      is Picture.WithImage -> poster.thumbnail.url
    },
    rating = vote.average.toString(),
    releaseDate = release.year.toString(),
    watchButtonModel = when {
      watchList -> WatchButtonModel.Selected
      else -> WatchButtonModel.Unselected
    }
  )
}