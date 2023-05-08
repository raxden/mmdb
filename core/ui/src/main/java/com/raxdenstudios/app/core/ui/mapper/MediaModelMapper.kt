package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class MediaModelMapper @Inject constructor(
    private val durationModelMapper: DurationModelMapper,
    private val dateModelMapper: DateModelMapper,
    private val languageModelMapper: LanguageModelMapper,
    private val currencyModelMapper: CurrencyModelMapper,
    private val ratingModelMapper: RatingModelMapper,
    private val pictureModelMapper: PictureModelMapper,
) : DataMapper<Media, MediaModel>() {

    override fun transform(source: Media): MediaModel = source.toModel()

    private fun Media.toModel() = when (this) {
        is Media.Movie -> MediaModel(
            id = id,
            mediaType = MediaType.Movie,
            title = title,
            overview = overview,
            backdrop = pictureModelMapper.transform(backdrop),
            poster = pictureModelMapper.transform(poster),
            certification = certification,
            genres = genres.joinToString { it.name },
            duration = durationModelMapper.transform(duration),
            rating = ratingModelMapper.transform(vote.average),
            releaseYear = release.year.toString(),
            releaseDate = dateModelMapper.transform(release),
            watchlist = watchList,
            originalLanguage = languageModelMapper.transform(originalLanguage),
            spokenLanguages = spokenLanguages.joinToString { languageModelMapper.transform(it) },
            budget = currencyModelMapper.transform(budget),
            revenue = currencyModelMapper.transform(revenue),
        )

        is Media.TVShow -> MediaModel(
            id = id,
            mediaType = MediaType.TvShow,
            title = name,
            overview = overview,
            backdrop = pictureModelMapper.transform(backdrop),
            poster = pictureModelMapper.transform(poster),
            certification = certification,
            genres = genres.joinToString { it.name },
            duration = durationModelMapper.transform(duration),
            rating = ratingModelMapper.transform(vote.average),
            releaseYear = firstAirDate.year.toString(),
            releaseDate = dateModelMapper.transform(firstAirDate),
            watchlist = watchList,
            originalLanguage = languageModelMapper.transform(originalLanguage),
            spokenLanguages = spokenLanguages.joinToString { languageModelMapper.transform(it) },
            budget = "",
            revenue = "",
        )
    }
}
