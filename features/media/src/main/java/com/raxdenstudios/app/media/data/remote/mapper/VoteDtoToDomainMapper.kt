package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Vote
import com.raxdenstudios.commons.util.DataMapper

internal class VoteDtoToDomainMapper : DataMapper<MediaDto, Vote>() {

  override fun transform(source: MediaDto): Vote = Vote(
    average = source.vote_average.toFloat(),
    count = source.vote_count
  )
}