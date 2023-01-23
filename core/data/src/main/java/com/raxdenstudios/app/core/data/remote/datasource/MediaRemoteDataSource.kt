package com.raxdenstudios.app.core.data.remote.datasource

import com.raxdenstudios.app.core.data.remote.mapper.MediaDetailDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.core.data.remote.mapper.PageDtoToPageListMapper
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.network.gateway.MediaGateway
import com.raxdenstudios.app.core.network.gateway.WatchlistGateway
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.core.model.Account
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
    private val mediaGateway: MediaGateway,
    private val watchlistGateway: WatchlistGateway,
    private val mediaDtoToDomainMapper: MediaDtoToDomainMapper,
    private val mediaDetailDtoToDomainMapper: MediaDetailDtoToDomainMapper,
    private val pageDtoToPageListMapper: PageDtoToPageListMapper,
    private val mediaTypeToDtoMapper: MediaTypeToDtoMapper,
) {

    suspend fun fetchById(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<Media> =
        mediaGateway.fetchById(mediaId, mediaType)
            .map { dto -> mediaDetailDtoToDomainMapper.transform(dto) }

    suspend fun fetch(
        mediaFilter: MediaFilter,
        page: Page,
        account: Account,
    ): ResultData<PageList<Media>> = when (mediaFilter.mediaCategory) {
        MediaCategory.NowPlaying -> mediaGateway.nowPlaying(mediaFilter.mediaType, page)
            .map { pageDto -> pageDtoToPageListMapper.transform(pageDto) }
        MediaCategory.Popular -> mediaGateway.popular(mediaFilter.mediaType, page)
            .map { pageDto -> pageDtoToPageListMapper.transform(pageDto) }
        MediaCategory.TopRated -> mediaGateway.topRated(mediaFilter.mediaType, page)
            .map { pageDto -> pageDtoToPageListMapper.transform(pageDto) }
        MediaCategory.Upcoming -> mediaGateway.upcoming(mediaFilter.mediaType, page)
            .map { pageDto -> pageDtoToPageListMapper.transform(pageDto) }
        MediaCategory.Watchlist -> fetchWatchlist(mediaFilter, page, account)
            .map { pageDto -> pageDtoToPageListMapper.transform(pageDto) }
            .map { pageList ->
                pageList.copy(
                    items = pageList.items.map { movie -> movie.copyWith(watchList = true) }
                )
            }
    }

    suspend fun addToWatchlist(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Boolean> = watchlistGateway.add(
        mediaId = mediaId,
        mediaType = mediaTypeToDtoMapper.transform(mediaType),
        accountId = account.credentials.accountId,
    )

    suspend fun removeFromWatchlist(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Boolean> = watchlistGateway.remove(
        mediaId = mediaId,
        mediaType = mediaTypeToDtoMapper.transform(mediaType),
        accountId = account.credentials.accountId,
    )

    suspend fun fetchWatchlist(
        mediaType: MediaType,
        account: Account,
    ): ResultData<List<Media>> = when (account) {
        is Account.Guest -> ResultData.Error(IllegalStateException("Guest account can't fetch watchlist"))
        is Account.Logged -> watchlistGateway.fetchAll(
            mediaType = mediaType,
            accountId = account.credentials.accountId
        )
            .map { list -> mediaDtoToDomainMapper.transform(list) }
            .map { list -> list.map { media -> media.copyWith(watchList = true) } }
    }

    private suspend fun fetchWatchlist(
        mediaFilter: MediaFilter,
        page: Page,
        account: Account,
    ): ResultData<PageDto<MediaDto>> = when (account) {
        is Account.Guest -> ResultData.Error(IllegalStateException("Guest account can't fetch watchlist"))
        is Account.Logged -> watchlistGateway.fetchByPage(
            mediaType = mediaFilter.mediaType,
            page = page,
            accountId = account.credentials.accountId
        )
    }
}

