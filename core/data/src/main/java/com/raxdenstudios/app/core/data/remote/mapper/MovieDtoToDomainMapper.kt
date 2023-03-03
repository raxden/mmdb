package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.network.model.MediaDto
import org.threeten.bp.Duration
import java.util.Locale
import javax.inject.Inject

class MovieDtoToDomainMapper @Inject constructor(
    private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
    private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
    private val genreDtoToDomainMapper: GenreDtoToDomainMapper,
    private val localeDtoToDomainMapper: LocaleDtoToDomainMapper,
) {

    fun transform(source: MediaDto.Movie): Media = source.toDomain()

    private fun MediaDto.Movie.toDomain() = Media.Movie(
        id = MediaId(id.toLong()),
        title = title,
        overview = overview,
        backdrop = pictureDtoToDomainMapper.transform(backdrop_path),
        poster = pictureDtoToDomainMapper.transform(poster_path),
        release = dateDtoToLocalDateMapper.transform(release_date),
        vote = voteDtoToDomainMapper.transform(this),
        watchList = false,
        duration = Duration.ofMinutes(0),
        genres = genre_ids.map { id -> genreDtoToDomainMapper.transform(id) },
        originalLanguage = localeDtoToDomainMapper.transform(original_language),
        spokenLanguages = emptyList(),
        budget = 0.0,
        revenue = 0.0,
    )
}
