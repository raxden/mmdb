package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.model.Vote
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class VoteDtoToDomainMapper @Inject constructor() : DataMapper<MediaDto, Vote>() {

    override fun transform(source: MediaDto): Vote = Vote(
        average = source.vote_average.toFloat(),
        count = source.vote_count
    )
}
