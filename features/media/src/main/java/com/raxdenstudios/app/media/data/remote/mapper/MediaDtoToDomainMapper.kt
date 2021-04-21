package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaType

internal class MediaDtoToDomainMapper(
  private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
  private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
  private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
) {

  fun transform(mediaType: MediaType, source: MediaDto): Media = source.toDomain(mediaType)

  private fun MediaDto.toDomain(mediaType: MediaType) = Media(
    id = id.toLong(),
    mediaType = mediaType,
    title = title,
    backdrop = pictureDtoToDomainMapper.transform(backdrop_path),
    poster = pictureDtoToDomainMapper.transform(poster_path),
    release = dateDtoToLocalDateMapper.transform(release_date),
    vote = voteDtoToDomainMapper.transform(this),
    watchList = false,
  )
}