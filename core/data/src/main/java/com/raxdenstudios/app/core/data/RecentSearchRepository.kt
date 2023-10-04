package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.data.local.datasource.RecentSearchLocalDataSource
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSearchRepository @Inject constructor(
    private val recentSearchLocalDataSource: RecentSearchLocalDataSource,
) {

    fun observe(): Flow<ResultData<List<String>, ErrorDomain>> =
        recentSearchLocalDataSource.observe()

    suspend fun save(query: String): ResultData<Boolean, ErrorDomain> =
        recentSearchLocalDataSource.add(query)
}
