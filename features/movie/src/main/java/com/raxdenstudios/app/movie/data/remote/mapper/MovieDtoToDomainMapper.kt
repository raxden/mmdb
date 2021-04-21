package com.raxdenstudios.app.movie.data.remote.mapper

import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.MediaType

internal class MovieDtoToDomainMapper(
  private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
  private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
  private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
) {

  fun transform(mediaType: MediaType, source: MovieDto): Media = source.toDomain(mediaType)

  private fun MovieDto.toDomain(mediaType: MediaType) = Media(
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
