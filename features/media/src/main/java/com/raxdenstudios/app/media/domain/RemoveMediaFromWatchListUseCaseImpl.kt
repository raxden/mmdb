package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.commons.ResultData
import javax.inject.Inject

internal class RemoveMediaFromWatchListUseCaseImpl @Inject constructor(
    private val mediaRepository: MediaRepository,
) : RemoveMediaFromWatchListUseCase {

    override suspend operator fun invoke(params: RemoveMediaFromWatchListUseCase.Params): ResultData<Boolean> =
        mediaRepository.removeFromWatchList(params.mediaId, params.mediaType)
}
