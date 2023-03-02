package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import org.threeten.bp.Duration
import javax.inject.Inject

class MovieDetailDtoToDomainMapper @Inject constructor(
    private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
    private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
    private val genreDtoToDomainMapper: GenreDtoToDomainMapper,
    private val certificationDtoToDomainMapper: CertificationDtoToDomainMapper,
) {

    fun transform(source: MediaDetailDto.Movie): Media = source.toDomain()

    private fun MediaDetailDto.Movie.toDomain() = Media.Movie(
        id = MediaId(id.toLong()),
        title = title,
        overview = overview,
        backdrop = pictureDtoToDomainMapper.transform(backdrop_path),
        poster = pictureDtoToDomainMapper.transform(poster_path),
        release = dateDtoToLocalDateMapper.transform(release_date),
        vote = voteDtoToDomainMapper.transform(this),
        watchList = false,
        duration = Duration.ofMinutes(runtime.toLong()),
        genres = genres.map { genre -> genreDtoToDomainMapper.transform(genre) },
        certification = certificationDtoToDomainMapper.transform(release_dates),
    )
}
