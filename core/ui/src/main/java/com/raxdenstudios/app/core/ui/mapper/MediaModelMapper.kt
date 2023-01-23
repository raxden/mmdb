package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Picture
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.model.WatchButtonModel
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class MediaModelMapper @Inject constructor(
    private val durationModelMapper: DurationModelMapper,
) : DataMapper<Media, MediaModel>() {

    override fun transform(source: Media): MediaModel = source.toModel()

    private fun Media.toModel() = when (this) {
        is Media.Movie -> MediaModel(
            id = id,
            mediaType = MediaType.Movie,
            title = title,
            overview = overview,
            backdrop = when (val backdrop = backdrop) {
                Picture.Empty -> ""
                is Picture.WithImage -> backdrop.thumbnail.url
            },
            poster = when (val poster = poster) {
                Picture.Empty -> ""
                is Picture.WithImage -> poster.thumbnail.url
            },
            contentRating = "",
            genres = genres.joinToString { it.name },
            duration = durationModelMapper.transform(duration),
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
            overview = overview,
            backdrop = when (val backdrop = backdrop) {
                Picture.Empty -> ""
                is Picture.WithImage -> backdrop.thumbnail.url
            },
            poster = when (val poster = poster) {
                Picture.Empty -> ""
                is Picture.WithImage -> poster.thumbnail.url
            },
            contentRating = "",
            genres = genres.joinToString { it.name },
            duration = durationModelMapper.transform(duration),
            rating = vote.average.toString(),
            releaseDate = firstAirDate.year.toString(),
            watchButton = when {
                watchList -> WatchButtonModel.Selected
                else -> WatchButtonModel.Unselected
            }
        )
    }
}
