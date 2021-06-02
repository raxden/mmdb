package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaId

internal class MovieDtoToDomainMapper(
  private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
  private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
  private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
) {

  fun transform(source: MediaDto.Movie): Media = source.toDomain()

  private fun MediaDto.Movie.toDomain() = Media.Movie(
    id = MediaId(id.toLong()),
    title = title,
    backdrop = pictureDtoToDomainMapper.transform(backdrop_path),
    poster = pictureDtoToDomainMapper.transform(poster_path),
    release = dateDtoToLocalDateMapper.transform(release_date),
    vote = voteDtoToDomainMapper.transform(this),
    watchList = false,
  )
}

