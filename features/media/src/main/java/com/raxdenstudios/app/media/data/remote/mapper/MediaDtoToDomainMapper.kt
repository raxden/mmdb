package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Media

internal class MediaDtoToDomainMapper(
  private val movieDtoToDomainMapper: MovieDtoToDomainMapper,
  private val tvShowDtoToDomainMapper: TVShowDtoToDomainMapper,
) {

  fun transform(source: MediaDto): Media = source.toDomain()

  private fun MediaDto.toDomain() = when (this) {
    is MediaDto.Movie -> movieDtoToDomainMapper.transform(this)
    is MediaDto.TVShow -> tvShowDtoToDomainMapper.transform(this)
  }
}

