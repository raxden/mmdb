package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Media

internal class TVShowDtoToDomainMapper(
  private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
  private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
  private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
) {

  fun transform(source: MediaDto.TVShow): Media = source.toDomain()

  private fun MediaDto.TVShow.toDomain() = Media.TVShow(
    id = id.toLong(),
    name = name,
    backdrop = pictureDtoToDomainMapper.transform(backdrop_path),
    poster = pictureDtoToDomainMapper.transform(poster_path),
    firstAirDate = dateDtoToLocalDateMapper.transform(first_air_date),
    vote = voteDtoToDomainMapper.transform(this),
    watchList = false,
  )
}