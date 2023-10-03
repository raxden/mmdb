package com.raxdenstudios.app.core.data.remote.datasource

import com.raxdenstudios.app.core.data.remote.mapper.MediaDetailDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.core.data.remote.mapper.NetworkErrorDtoToErrorMapper
import com.raxdenstudios.app.core.data.remote.mapper.VideoDtoToDomainMapper
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.network.gateway.MediaGateway
import com.raxdenstudios.app.core.network.gateway.WatchlistGateway
import com.raxdenstudios.app.core.network.model.ErrorDto
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.app.core.network.model.toPageList
import com.raxdenstudios.commons.NetworkError
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.mapFailure
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.core.model.Account
import javax.inject.Inject

@Suppress("LongParameterList")
class MediaRemoteDataSource @Inject constructor(
    private val mediaGateway: MediaGateway,
    private val watchlistGateway: WatchlistGateway,
    private val mediaDtoToDomainMapper: MediaDtoToDomainMapper,
    private val mediaDetailDtoToDomainMapper: MediaDetailDtoToDomainMapper,
    private val mediaTypeToDtoMapper: MediaTypeToDtoMapper,
    private val videoDtoToDomainMapper: VideoDtoToDomainMapper,
    private val networkErrorDtoToErrorMapper: NetworkErrorDtoToErrorMapper,
) {

    suspend fun fetchById(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<Media, ErrorDomain> =
        mediaGateway.fetchById(mediaId, mediaType)
            .map { dto -> mediaDetailDtoToDomainMapper.transform(dto) }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun fetch(
        mediaFilter: MediaFilter,
        page: Page,
        account: Account,
    ): ResultData<PageList<Media>, ErrorDomain> = when (mediaFilter.mediaCategory) {
        MediaCategory.NowPlaying -> mediaGateway.nowPlaying(mediaFilter.mediaType, page).toDomain()
        MediaCategory.Popular -> mediaGateway.popular(mediaFilter.mediaType, page).toDomain()
        MediaCategory.TopRated -> mediaGateway.topRated(mediaFilter.mediaType, page).toDomain()
        MediaCategory.Upcoming -> mediaGateway.upcoming(mediaFilter.mediaType, page).toDomain()
        MediaCategory.Watchlist -> when (account) {
            is Account.Guest -> ResultData.Failure(ErrorDomain.Unauthorized("Guest account can't fetch watchlist"))
            is Account.Logged -> watchlistGateway.fetch(
                mediaType = mediaFilter.mediaType,
                page = page,
                accountId = account.credentials.accountId
            ).toDomain().markAsWatchlist()
        }
    }

    private fun ResultData<PageList<Media>, ErrorDomain>.markAsWatchlist() =
        map { pageList -> pageList.copy(items = pageList.items.map { movie -> movie.copyWith(watchList = true) }) }

    private fun ResultData<PageDto<MediaDto>, NetworkError<ErrorDto>>.toDomain() =
        map { pageDto -> pageDto.toPageList { results -> mediaDtoToDomainMapper.transform(results) } }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun addToWatchlist(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Boolean, ErrorDomain> = watchlistGateway.add(
        mediaId = mediaId,
        mediaType = mediaTypeToDtoMapper.transform(mediaType),
        accountId = account.credentials.accountId,
    ).mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun removeFromWatchlist(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Boolean, ErrorDomain> = watchlistGateway.remove(
        mediaId = mediaId,
        mediaType = mediaTypeToDtoMapper.transform(mediaType),
        accountId = account.credentials.accountId,
    ).mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun fetchWatchlist(
        mediaType: MediaType,
        account: Account,
    ): ResultData<List<Media>, ErrorDomain> = when (account) {
        is Account.Guest -> ResultData.Failure(ErrorDomain.Unauthorized("Guest account can't fetch watchlist"))
        is Account.Logged -> watchlistGateway.fetchAll(
            mediaType = mediaType,
            accountId = account.credentials.accountId
        )
            .map { list -> mediaDtoToDomainMapper.transform(list) }
            .map { list -> list.map { media -> media.copyWith(watchList = true) } }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }
    }

    suspend fun videos(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<List<Video>, ErrorDomain> =
        mediaGateway.videos(mediaId, mediaType)
            .map { pageDto -> videoDtoToDomainMapper.transform(pageDto.results) }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun related(
        mediaId: MediaId,
        mediaType: MediaType,
        page: Page,
    ): ResultData<PageList<Media>, ErrorDomain> =
        mediaGateway.related(mediaId, mediaType, page)
            .map { pageDto -> pageDto.toPageList { results -> mediaDtoToDomainMapper.transform(results) } }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun search(
        query: String,
        page: Page,
    ): ResultData<PageList<Media>, ErrorDomain> =
        mediaGateway.search(query, page)
            .map { pageDto -> pageDto.toPageList { results -> mediaDtoToDomainMapper.transform(results) } }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }
}
