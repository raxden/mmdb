package com.raxdenstudios.app.media.view.mapper

import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.domain.model.Picture
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.util.DataMapper
import javax.inject.Inject

class MediaListItemModelMapper @Inject constructor() : DataMapper<Media, MediaListItemModel>() {

  override fun transform(source: Media): MediaListItemModel = source.toModel()

  private fun Media.toModel() = when (this) {
    is Media.Movie -> MediaListItemModel(
      id = id,
      mediaType = MediaType.MOVIE,
      title = title,
      backdrop = when (val backdrop = backdrop) {
        Picture.Empty -> ""
        is Picture.WithImage -> backdrop.thumbnail.url
      },
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
      backdrop = when (val backdrop = backdrop) {
        Picture.Empty -> ""
        is Picture.WithImage -> backdrop.thumbnail.url
      },
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
