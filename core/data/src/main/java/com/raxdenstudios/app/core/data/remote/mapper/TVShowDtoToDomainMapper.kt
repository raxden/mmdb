package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import javax.inject.Inject

class TVShowDtoToDomainMapper @Inject constructor(
    private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
    private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
) {

    fun transform(source: MediaDto.TVShow): Media = source.toDomain()

    private fun MediaDto.TVShow.toDomain() = Media.TVShow(
        id = MediaId(id.toLong()),
        name = name,
        overview = overview,
        backdrop = pictureDtoToDomainMapper.transform(backdrop_path),
        poster = pictureDtoToDomainMapper.transform(poster_path),
        firstAirDate = dateDtoToLocalDateMapper.transform(first_air_date),
        vote = voteDtoToDomainMapper.transform(this),
        watchList = false,
    )
}