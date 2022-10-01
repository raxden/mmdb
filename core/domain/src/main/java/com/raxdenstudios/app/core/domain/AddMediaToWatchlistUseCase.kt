package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import javax.inject.Inject

class AddMediaToWatchlistUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(params: Params): ResultData<Media> =
        mediaRepository.addToWatchlist(params.mediaId, params.mediaType)

    data class Params(
        val mediaId: MediaId,
        val mediaType: MediaType,
    )
}
