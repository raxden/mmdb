package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun medias(
        mediaFilter: MediaFilter,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain>

    suspend fun fetchById(
        mediaId: MediaId,
        mediaType: MediaType,
    ): Flow<ResultData<Media, ErrorDomain>>

    fun observeWatchlist(): Flow<ResultData<List<Media>, ErrorDomain>>

    suspend fun addToWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Media, ErrorDomain>

    suspend fun removeFromWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Boolean, ErrorDomain>

    suspend fun videos(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<List<Video>, ErrorDomain>

    suspend fun related(
        mediaId: MediaId,
        mediaType: MediaType,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain>
}
