package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.RecentSearchRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LastRecentSearchesUseCase @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository,
) {

    operator fun invoke(): Flow<ResultData<List<String>, ErrorDomain>> =
        recentSearchRepository.observe()
            .map { resultData ->
                resultData.map { value -> value.takeLast(MAX_RECENT_SEARCHES) }
            }

    companion object {
        const val MAX_RECENT_SEARCHES = 5
    }
}
