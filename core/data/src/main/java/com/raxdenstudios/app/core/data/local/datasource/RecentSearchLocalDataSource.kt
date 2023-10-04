package com.raxdenstudios.app.core.data.local.datasource

import androidx.room.Transaction
import com.raxdenstudios.app.core.data.local.mapper.ExceptionToErrorMapper
import com.raxdenstudios.app.core.database.RecentSearchDatabase
import com.raxdenstudios.app.core.database.dao.RecentSearchDao
import com.raxdenstudios.app.core.database.model.RecentSearchEntity
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.coRunCatching
import com.raxdenstudios.commons.core.ext.mapFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentSearchLocalDataSource @Inject constructor(
    private val database: RecentSearchDatabase,
    private val exceptionToErrorMapper: ExceptionToErrorMapper,
) {

    private val recentSearchDao: RecentSearchDao
        get() = database.recentSearchDao()

    fun observe(): Flow<ResultData<List<String>, ErrorDomain>> =
        recentSearchDao.observe()
            .map { entityList -> entityList.map { entity -> entity.query } }
            .map { query -> ResultData.Success(query) }

    @Transaction
    suspend fun add(query: String): ResultData<Boolean, ErrorDomain> = coRunCatching {
        recentSearchDao.insert(RecentSearchEntity(query = query))
        true
    }.mapFailure { error -> exceptionToErrorMapper.transform(error) }
}
