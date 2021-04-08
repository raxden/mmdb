package com.raxdenstudios.app.movie.data.remote.mapper

import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.commons.util.DataMapper

internal class MovieDtoToDomainMapper(
  private val voteDtoToDomainMapper: VoteDtoToDomainMapper,
  private val pictureDtoToDomainMapper: PictureDtoToDomainMapper,
  private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
) : DataMapper<MovieDto, Movie>() {

  override fun transform(source: MovieDto): Movie = source.toDomain()

  private fun MovieDto.toDomain() = Movie(
    id = id.toLong(),
    title = title,
    backdrop = backdrop_path?.let { path -> pictureDtoToDomainMapper.transform(path) },
    poster = pictureDtoToDomainMapper.transform(poster_path),
    release = dateDtoToLocalDateMapper.transform(release_date),
    vote = voteDtoToDomainMapper.transform(this),
  )
}
