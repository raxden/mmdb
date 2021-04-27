package com.raxdenstudios.app.media.view.mapper

import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.domain.model.Picture
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.util.DataMapper

class MediaListItemModelMapper : DataMapper<Media, MediaListItemModel>() {

  override fun transform(source: Media): MediaListItemModel = source.toModel()

  private fun Media.toModel() = when (this) {
    is Media.Movie -> MediaListItemModel(
      id = id,
      mediaType = MediaType.MOVIE,
      title = title,
      image = when (val poster = poster) {
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
    is Media.TVShow -> MediaListItemModel(
      id = id,
      mediaType = MediaType.TV_SHOW,
      title = name,
      image = when (val poster = poster) {
        Picture.Empty -> ""
        is Picture.WithImage -> poster.thumbnail.url
      },
      rating = vote.average.toString(),
      releaseDate = firstAirDate.year.toString(),
      watchButtonModel = when {
        watchList -> WatchButtonModel.Selected
        else -> WatchButtonModel.Unselected
      }
    )
  }
}