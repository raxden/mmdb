package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import javax.inject.Inject

class MovieDtoToDomainMapper @Inject constructor(
    private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
    private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
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
    )
}

