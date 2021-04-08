package com.raxdenstudios.app.movie.data.remote.mapper

import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.domain.Vote
import com.raxdenstudios.commons.util.DataMapper

internal class VoteDtoToDomainMapper : DataMapper<MovieDto, Vote>() {

  override fun transform(source: MovieDto): Vote = Vote(
    average = source.vote_average.toFloat(),
    count = source.vote_count
  )
}
