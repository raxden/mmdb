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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeMediaRepository : MediaRepository {

    private val watchlist = MutableStateFlow(
        listOf(
            Media.Movie.mock.copy(id = MediaId(3))
        )
    )
    private val movies: List<Media.Movie> = listOf(
        Media.Movie.mock.copy(id = MediaId(1)),
        Media.Movie.mock.copy(id = MediaId(2)),
        Media.Movie.mock.copy(id = MediaId(3)),
    )
    private val pagedMedias: Map<Page, PageList<Media>> = mapOf(
        Page(1) to PageList(
            page = Page(1),
            items = listOf(
                Media.Movie.mock.copy(id = MediaId(1)),
                Media.Movie.mock.copy(id = MediaId(2)),
                Media.Movie.mock.copy(id = MediaId(3)),
            ),
        ),
        Page(2) to PageList(
            page = Page(2),
            items = listOf(
                Media.Movie.mock.copy(id = MediaId(2)),
                Media.Movie.mock.copy(id = MediaId(3)),
                Media.Movie.mock.copy(id = MediaId(4)),
            ),
        ),
    )

    override suspend fun medias(
        mediaFilter: MediaFilter,
        page: Page,
        pageSize: PageSize
    ): ResultData<PageList<Media>, ErrorDomain> {
        return ResultData.Success(pagedMedias[page]!!)
    }

    override fun fetchById(mediaId: MediaId, mediaType: MediaType): Flow<ResultData<Media, ErrorDomain>> {
        TODO("Not yet implemented")
    }

    override fun observeWatchlist(): Flow<ResultData<List<Media>, ErrorDomain>> {
        return watchlist.map { value -> ResultData.Success(value) }
    }

    override suspend fun addToWatchlist(mediaId: MediaId, mediaType: MediaType): ResultData<Media, ErrorDomain> {
        val mediaToAdd = Media.Movie.mock.copy(id = mediaId, watchList = true)
        watchlist.update { value ->
            value.toMutableList().also { list ->
                list.add(mediaToAdd)
            }
        }
        return ResultData.Success(mediaToAdd)
    }

    override suspend fun removeFromWatchlist(mediaId: MediaId, mediaType: MediaType): ResultData<Boolean, ErrorDomain> {
        val mediaToRemove = Media.Movie.mock.copy(id = mediaId)
        watchlist.update { value ->
            value.toMutableList().also { list ->
                list.remove(mediaToRemove)
            }
        }
        return ResultData.Success(true)
    }

    override suspend fun videos(mediaId: MediaId, mediaType: MediaType): ResultData<List<Video>, ErrorDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun related(
        mediaId: MediaId,
        mediaType: MediaType,
        page: Page,
        pageSize: PageSize
    ): ResultData<PageList<Media>, ErrorDomain> {
        return ResultData.Success(PageList(movies, page))
    }



    override suspend fun search(query: String, page: Page, pageSize: PageSize): ResultData<PageList<Media>, ErrorDomain> {
        return ResultData.Success(pagedMedias[page]!!)
    }
}
