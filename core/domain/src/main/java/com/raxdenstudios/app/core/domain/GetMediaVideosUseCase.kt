package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.core.ResultData
import javax.inject.Inject

class GetMediaVideosUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(
        params: Params,
    ): ResultData<List<Video>, ErrorDomain> =
        mediaRepository.videos(params.mediaId, params.mediaType)

    data class Params(
        val mediaId: MediaId,
        val mediaType: MediaType,
    )
}
