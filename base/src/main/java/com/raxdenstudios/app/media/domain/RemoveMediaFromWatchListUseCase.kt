package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData

interface RemoveMediaFromWatchListUseCase {
    suspend operator fun invoke(params: Params): ResultData<Boolean>

    data class Params(
        val mediaId: MediaId,
        val mediaType: MediaType,
    )
}
