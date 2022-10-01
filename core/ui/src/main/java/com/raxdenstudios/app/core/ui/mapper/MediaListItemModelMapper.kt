package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Picture
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.model.WatchButtonModel
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class MediaListItemModelMapper @Inject constructor() : DataMapper<Media, MediaModel>() {

    override fun transform(source: Media): MediaModel = source.toModel()

    private fun Media.toModel() = when (this) {
        is Media.Movie -> MediaModel(
            id = id,
            mediaType = MediaType.Movie,
            title = title,
            description = overview,
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
            watchButton = when {
                watchList -> WatchButtonModel.Selected
                else -> WatchButtonModel.Unselected
            }
        )
        is Media.TVShow -> MediaModel(
            id = id,
            mediaType = MediaType.TvShow,
            title = name,
            description = overview,
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
            watchButton = when {
                watchList -> WatchButtonModel.Selected
                else -> WatchButtonModel.Unselected
            }
        )
    }
}
