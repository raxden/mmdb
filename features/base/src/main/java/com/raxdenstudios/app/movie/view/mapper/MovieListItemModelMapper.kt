package com.raxdenstudios.app.movie.view.mapper

import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.Picture
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.app.movie.view.model.WatchButtonModel
import com.raxdenstudios.commons.util.DataMapper

class MovieListItemModelMapper : DataMapper<Media, MediaListItemModel>() {

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