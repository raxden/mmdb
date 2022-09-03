package com.raxdenstudios.app.media.data.remote.datasource

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.media.data.remote.MediaGateway
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.data.remote.mapper.MediaDtoToDomainMapper
import com.raxdenstudios.app.media.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.coFlatMap
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import javax.inject.Inject

internal class MediaRemoteDataSource @Inject constructor(
    private val mediaGateway: MediaGateway,
    private val mediaTypeToDtoMapper: MediaTypeToDtoMapper,
    private val mediaDtoToDomainMapper: MediaDtoToDomainMapper,
) {

    suspend fun findById(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<Media> = when (mediaType) {
        MediaType.MOVIE -> mediaGateway.detailMovie(mediaId)
        MediaType.TV_SHOW -> mediaGateway.detailTVShow(mediaId)
    }.map { dto -> mediaDtoToDomainMapper.transform(dto) }

    suspend fun addToWatchList(
        account: Account,
        mediaType: MediaType,
        mediaId: MediaId,
    ): ResultData<Media> = when (account) {
        is Account.Guest -> ResultData.Error(UserNotLoggedException())
        is Account.Logged -> mediaGateway.addToWatchList(
            accountId = account.credentials.accountId,
            mediaType = mediaTypeToDtoMapper.transform(mediaType),
            mediaId = mediaId
        )
            .coFlatMap { findById(mediaId, mediaType) }
            .map { media -> media.copyWith(watchList = true) }
    }

    suspend fun removeFromWatchList(
        account: Account,
        mediaType: MediaType,
        mediaId: MediaId,
    ): ResultData<Boolean> = when (account) {
        is Account.Guest -> ResultData.Error(UserNotLoggedException())
        is Account.Logged -> mediaGateway.removeFromWatchList(
            accountId = account.credentials.accountId,
            mediaType = mediaTypeToDtoMapper.transform(mediaType),
            mediaId = mediaId
        )
    }

    suspend fun watchList(
        account: Account,
        mediaType: MediaType,
    ): ResultData<List<Media>> = when (account) {
        is Account.Guest -> ResultData.Error(UserNotLoggedException())
        is Account.Logged -> when (mediaType) {
            MediaType.MOVIE -> mediaGateway.watchListMovies(account.credentials.accountId)
            MediaType.TV_SHOW -> mediaGateway.watchListTVShows(account.credentials.accountId)
        }.map { list ->
            list.map { dto -> mediaDtoToDomainMapper.transform(dto).copyWith(watchList = true) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun medias(
        mediaFilter: MediaFilter,
        account: Account,
        page: Page,
    ): ResultData<PageList<Media>> = when (mediaFilter) {
        is MediaFilter.NowPlaying -> nowPlaying(page) as ResultData<PageList<Media>>
        is MediaFilter.Popular -> popular(mediaFilter.mediaType, page)
        is MediaFilter.TopRated -> topRated(mediaFilter.mediaType, page)
        MediaFilter.Upcoming -> upcoming(page) as ResultData<PageList<Media>>
        is MediaFilter.WatchList -> watchList(account, mediaFilter.mediaType, page)
    }

    private suspend fun watchList(
        account: Account,
        mediaType: MediaType,
        page: Page,
    ): ResultData<PageList<Media>> = when (account) {
        is Account.Guest -> ResultData.Error(UserNotLoggedException())
        is Account.Logged -> when (mediaType) {
            MediaType.MOVIE -> mediaGateway.watchListMovies(
                accountId = account.credentials.accountId,
                page = page.value
            )
            MediaType.TV_SHOW -> mediaGateway.watchListTVShows(
                accountId = account.credentials.accountId,
                page = page.value
            )
        }
            .map { pageDto -> transformMediaDtoPageData(pageDto) }
            .map { pageList -> markMediasAsWatched(pageList) }
    }

    private fun markMediasAsWatched(pageList: PageList<Media>) =
        pageList.copy(items = pageList.items.map { movie -> movie.copyWith(watchList = true) })

    @Suppress("UNCHECKED_CAST")
    private suspend fun upcoming(page: Page): ResultData<PageList<Media.Movie>> =
        mediaGateway.upcoming(page.value)
            .map { pageDto -> transformMediaDtoPageData(pageDto) as PageList<Media.Movie> }

    private suspend fun topRated(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
        when (mediaType) {
            MediaType.MOVIE -> mediaGateway.topRatedMovies(page.value)
            MediaType.TV_SHOW -> mediaGateway.topRatedTVShows(page.value)
        }.map { pageDto -> transformMediaDtoPageData(pageDto) }

    private suspend fun popular(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
        when (mediaType) {
            MediaType.MOVIE -> mediaGateway.popularMovies(page.value)
            MediaType.TV_SHOW -> mediaGateway.popularTVShows(page.value)
        }.map { pageDto -> transformMediaDtoPageData(pageDto) }

    @Suppress("UNCHECKED_CAST")
    private suspend fun nowPlaying(page: Page): ResultData<PageList<Media.Movie>> =
        mediaGateway.nowPlaying(page.value)
            .map { pageDto -> transformMediaDtoPageData(pageDto) as PageList<Media.Movie> }

    private fun transformMediaDtoPageData(
        pageDto: PageDto<out MediaDto>,
    ): PageList<Media> =
        pageDto.toPageList { mediaDtoList ->
            mediaDtoList.map { mediaDto -> mediaDtoToDomainMapper.transform(mediaDto) }
        }
}
