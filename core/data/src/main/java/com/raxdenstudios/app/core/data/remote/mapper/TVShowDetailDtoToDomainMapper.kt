package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import org.threeten.bp.Duration
import javax.inject.Inject

class TVShowDetailDtoToDomainMapper @Inject constructor(
    private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
    private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
    private val genreDtoToDomainMapper: GenreDtoToDomainMapper,
    private val contentRatingDtoToDomainMapper: ContentRatingDtoToDomainMapper,
    private val localeDtoToDomainMapper: LocaleDtoToDomainMapper,
) {

    fun transform(source: MediaDetailDto.TVShow): Media = source.toDomain()

    private fun MediaDetailDto.TVShow.toDomain() = Media.TVShow(
        id = MediaId(id.toLong()),
        name = name,
        overview = overview,
        backdrop = pictureDtoToDomainMapper.transform(backdrop_path),
        poster = pictureDtoToDomainMapper.transform(poster_path),
        firstAirDate = dateDtoToLocalDateMapper.transform(first_air_date),
        vote = voteDtoToDomainMapper.transform(this),
        watchList = false,
        duration = episode_run_time.firstOrNull()?.let { duration ->
            Duration.ofMinutes(duration.toLong())
        } ?: Duration.ofMinutes(0),
        genres = genres.map { genre -> genreDtoToDomainMapper.transform(genre) },
        certification = contentRatingDtoToDomainMapper.transform(content_ratings),
        originalLanguage = localeDtoToDomainMapper.transform(original_language),
        spokenLanguages = spoken_languages.map { localeDtoToDomainMapper.transform(it.iso_639_1) },
    )
}
