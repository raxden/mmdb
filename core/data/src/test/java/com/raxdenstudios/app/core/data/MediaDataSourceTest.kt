package com.raxdenstudios.app.core.data

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.core.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.core.model.Account
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MediaDataSourceTest {

    private val mediaRemoteDataSource: MediaRemoteDataSource = mockk()
    private val accountLocalDataSource: AccountLocalDataSource = mockk()

    private lateinit var mediaDataSource: MediaDataSource

    @Before
    fun setUp() {
        mediaDataSource = MediaDataSource(
            mediaRemoteDataSource = mediaRemoteDataSource,
            accountLocalDataSource = accountLocalDataSource,
        )
    }

    @Test
    fun `fetch should return PageList of Media`() = runTest {
        val mediaFilter = MediaFilter.nowPlaying
        val page = Page(1)
        val pageSize = PageSize(20)
        coEvery {
            mediaRemoteDataSource.fetch(mediaFilter, page, any())
        } returns ResultData.Success(PageList(listOf(Media.Movie.mock), page))
        coEvery {
            accountLocalDataSource.getAccount()
        } returns Account.Logged.empty

        val result = mediaDataSource.fetch(mediaFilter, page, pageSize)

        assertThat(result).isEqualTo(ResultData.Success(PageList(listOf(Media.Movie.mock), page)))
    }

    @Test
    fun `fetchById should return Media`() = runTest {
        val mediaId = MediaId(1L)
        val mediaType = MediaType.Movie
        coEvery {
            mediaRemoteDataSource.fetchById(mediaId, mediaType)
        } returns ResultData.Success(Media.Movie.mock)

        val result = mediaDataSource.fetchById(mediaId, mediaType)

        assertThat(result).isEqualTo(ResultData.Success(Media.Movie.mock))
    }

    @Test
    fun `videos should return list of videos`() = runTest {
        val mediaId = MediaId(1L)
        val mediaType = MediaType.Movie
        coEvery {
            mediaRemoteDataSource.videos(mediaId, mediaType)
        } returns ResultData.Success(listOf(Video.mock))

        val result = mediaDataSource.videos(mediaId, mediaType)

        assertThat(result).isEqualTo(ResultData.Success(listOf(Video.mock)))
    }
}
