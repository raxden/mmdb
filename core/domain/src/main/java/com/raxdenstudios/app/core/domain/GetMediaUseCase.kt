package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(
        params: Params,
    ): Flow<ResultData<Media, ErrorDomain>> =
        mediaRepository.fetchById(params.mediaId, params.mediaType)

    data class Params(
        val mediaId: MediaId,
        val mediaType: MediaType,
    )
}
