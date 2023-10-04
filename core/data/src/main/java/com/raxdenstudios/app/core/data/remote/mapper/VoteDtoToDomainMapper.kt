package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.model.Vote
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import com.raxdenstudios.commons.core.util.DataMapper
import javax.inject.Inject


class VoteDtoToDomainMapper @Inject constructor() {

    fun transform(source: MediaDto): Vote = Vote(
        average = source.vote_average.toFloat(),
        count = source.vote_count
    )

    fun transform(source: MediaDetailDto): Vote = Vote(
        average = source.vote_average.toFloat(),
        count = source.vote_count
    )
}
