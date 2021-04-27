package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.media.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.media.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.media.di.mediaDataModule
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class MediaRepositoryImplTest : BaseTest() {

  private val accountLocalDataSource: AccountLocalDataSource = mockk {
    coEvery { getAccount() } returns aAccountLogged
  }
  private val mediaRemoteDataSource: MediaRemoteDataSource = mockk {
    coEvery {
      addToWatchList(aAccountLogged, aMediaType, aMovieId)
    } returns ResultData.Success(aMovie.copy(watchList = true))
    coEvery {
      removeFromWatchList(aAccountLogged, aMediaType, aMovieId)
    } returns ResultData.Success(true)
    coEvery {
      medias(any(), aAccountLogged, aPage)
    } returns ResultData.Success(aPageList)
    coEvery {
      findById(aMovieId, aMediaType)
    } returns ResultData.Success(Media.Movie.empty.copy(id = aMovieId))
    coEvery {
      watchList(aAccountLogged, aMediaType)
    } returns ResultData.Success(aMovies)
  }
  private val mediaLocalDataSource: MediaLocalDataSource = mockk {
    coEvery { containsInWatchList(any()) } returns false
    coEvery { addToWatchList(any<Media>()) } returns Unit
    coEvery { addToWatchList(any<List<Media>>()) } returns ResultData.Success(true)
    coEvery { removeFromWatchList(any()) } returns Unit
  }
  private val apiDataProvider: com.raxdenstudios.app.network.APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      mediaDataModule,
      module {
        factory(override = true) { accountLocalDataSource }
        factory(override = true) { mediaRemoteDataSource }
        factory(override = true) { mediaLocalDataSource }
        factory(override = true) { apiDataProvider }
      }
    )

  private val repository: MediaRepository by inject()

  @Test
  fun `Given a list of medias, When addToLocalWatchList is called, Then medias are persisted`() =
    testDispatcher.runBlockingTest {
      val result = repository.addToLocalWatchList(aMovies)

      coVerify { mediaLocalDataSource.addToWatchList(aMovies) }
      assertEquals(ResultData.Success(true), result)
    }

  @Test
  fun `Given a mediaType, When watchList is called, Then watchList is returned`() =
    testDispatcher.runBlockingTest {
      val result = repository.watchList(MediaType.MOVIE)

      assertEquals(
        ResultData.Success(
          listOf(
            Media.Movie.empty.copy(id = 1L),
            Media.Movie.empty.copy(id = 2L),
          )
        ), result
      )
    }

  @Test
  fun `Given a movie, When addMovieToWatchList is called, Then movie is added to watchlist`() =
    testDispatcher.runBlockingTest {
      val result = repository.addToWatchList(aMovieId, aMediaType)

      coVerify { mediaLocalDataSource.addToWatchList(aMovie.copy(watchList = true)) }
      assertEquals(ResultData.Success(aMovie.copy(watchList = true)), result)
    }

  @Test
  fun `Given a movie, When removeMovieFromWatchList is called, Then movie is removed`() =
    testDispatcher.runBlockingTest {
      val result = repository.removeFromWatchList(aMovieId, aMediaType)

      coVerify { mediaLocalDataSource.removeFromWatchList(aMovieId) }
      assertEquals(ResultData.Success(true), result)
    }

  @Test
  fun `Given a movies returned by the server, When movies are called, Then movies returned should be marked as watched if requires`() =
    testDispatcher.runBlockingTest {
      coEvery { mediaLocalDataSource.containsInWatchList(2L) } returns true

      val result = repository.medias(MediaFilter.popularMovies, aPage, aPageSize)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Media.Movie.empty.copy(id = 1L),
              Media.Movie.empty.copy(id = 2L, watchList = true),
            ),
            page = Page(1),
          )
        ),
        result
      )
    }
}

private val aMediaType = MediaType.MOVIE
private val aMovies = listOf(
  Media.Movie.empty.copy(id = 1L),
  Media.Movie.empty.copy(id = 2L),
)
private val aPage = Page(1)
private val aPageSize = PageSize.defaultSize
private val aPageList = PageList<Media>(
  items = aMovies,
  page = aPage,
)
private const val aCredentialsAccountId = "aCredentialsAccountId"
private val aAccountGuest = Account.Guest.empty
private val aAccountLogged = Account.Logged.empty.copy(
  credentials = Credentials.empty.copy(
    accountId = aCredentialsAccountId
  )
)
private const val aMovieId = 1L
private val aMovie = Media.Movie.empty.copy(id = aMovieId)