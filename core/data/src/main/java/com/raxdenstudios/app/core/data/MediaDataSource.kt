package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.core.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import javax.inject.Inject

class MediaDataSource @Inject constructor(
    private val mediaRemoteDataSource: MediaRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
) {

    @Suppress("UnusedPrivateMember")
    suspend fun fetch(
        mediaFilter: MediaFilter,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain> =
        mediaRemoteDataSource.fetch(
            mediaFilter = mediaFilter,
            page = page,
            account = accountLocalDataSource.getAccount()
        )

    suspend fun fetchById(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<Media, ErrorDomain> =
        mediaRemoteDataSource.fetchById(
            mediaId = mediaId,
            mediaType = mediaType,
        )

    @Suppress("UnusedPrivateMember")
    suspend fun videos(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<List<Video>, ErrorDomain> =
        mediaRemoteDataSource.videos(
            mediaId = mediaId,
            mediaType = mediaType,
        )

    @Suppress("UnusedPrivateMember")
    suspend fun related(
        mediaId: MediaId,
        mediaType: MediaType,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain> =
        mediaRemoteDataSource.related(
            mediaId = mediaId,
            mediaType = mediaType,
            page = page,
        )

    @Suppress("UnusedPrivateMember")
    suspend fun search(
        query: String,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain> =
        mediaRemoteDataSource.search(
            query = query,
            page = page,
        )
}
