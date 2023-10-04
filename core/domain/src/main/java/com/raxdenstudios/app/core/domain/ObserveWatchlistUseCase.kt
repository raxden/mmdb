package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWatchlistUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
) {

    operator fun invoke(): Flow<ResultData<List<Media>, ErrorDomain>> =
        mediaRepository.observeWatchlist()
}
