package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.movie.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.movie.data.remote.datasource.MovieRemoteDataSource
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.app.network.APIDataProvider
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
  private val movieRemoteDataSource: MovieRemoteDataSource = mockk {
    coEvery {
      addMovieToWatchList(aAccountLogged, aMediaType, aMovieId)
    } returns ResultData.Success(true)
    coEvery {
      removeMovieFromWatchList(aAccountLogged, aMediaType, aMovieId)
    } returns ResultData.Success(true)
    coEvery {
      movies(any(), aAccountLogged, aPage)
    } returns ResultData.Success(aPageList)
    coEvery {
      movieById(aMovieId, aMediaType)
    } returns ResultData.Success(Media.empty.copy(id = aMovieId))
    coEvery {
      watchList(aAccountLogged, aMediaType)
    } returns ResultData.Success(aMovies)
  }
  private val mediaLocalDataSource: MediaLocalDataSource = mockk {
    coEvery { insert(any<Media>()) } returns Unit
    coEvery { insert(any<List<Media>>()) } returns Unit
    coEvery { isWatchList(any()) } returns false
  }
  private val apiDataProvider: APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      movieDataModule,
      module {
        factory(override = true) { accountLocalDataSource }
        factory(override = true) { movieRemoteDataSource }
        factory(override = true) { mediaLocalDataSource }
        factory(override = true) { apiDataProvider }
      }
    )

  private val repository: MediaRepository by inject()

  @Test
  fun `Given a movie, When addMovieToWatchList is called, Then movie is added`() =
    testDispatcher.runBlockingTest {
      val result = repository.addMediaToWatchList(aMovieId, aMediaType)

      coVerify { mediaLocalDataSource.insert(aMovie.copy(watchList = true)) }
      assertEquals(ResultData.Success(true), result)
    }

  @Test
  fun `Given a movie, When removeMovieFromWatchList is called, Then movie is removed`() =
    testDispatcher.runBlockingTest {
      val result = repository.removeMediaFromWatchList(aMovieId, aMediaType)

      coVerify { mediaLocalDataSource.insert(aMovie.copy(watchList = false)) }
      assertEquals(ResultData.Success(true), result)
    }

  @Test
  fun `Given a movies returned by the server, When movies are called, Then movies returned should be marked as watched if requires`() =
    testDispatcher.runBlockingTest {
      coEvery { mediaLocalDataSource.isWatchList(2L) } returns true

      val result = repository.medias(MediaFilter.popularMovies, aPage, aPageSize)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Media.empty.copy(id = 1L),
              Media.empty.copy(id = 2L, watchList = true),
            ),
            page = Page(1),
          )
        ),
        result
      )
    }

  @Test
  fun `Given an account logged, When loadWatchListFromRemoteAndPersistInLocal is called, Then load data from remote and persist in local`() =
    testDispatcher.runBlockingTest {

      val result = repository.loadWatchListInLocal(aMediaType)

      assertEquals(ResultData.Success(true), result)
    }

  @Test
  fun `Given an account not logged, When loadWatchListFromRemoteAndPersistInLocal is called, Then and exception raised`() =
    testDispatcher.runBlockingTest {
      coEvery { accountLocalDataSource.getAccount() } returns aAccountGuest

      val result = repository.loadWatchListInLocal(aMediaType)

      result as ResultData.Error
      assert(result.throwable is UserNotLoggedException)
    }
}

private val aMediaType = MediaType.Movie
private val aMovies = listOf(
  Media.empty.copy(id = 1L),
  Media.empty.copy(id = 2L),
)
private val aPage = Page(1)
private val aPageSize = PageSize.defaultSize
private val aPageList = PageList(
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
private val aMovie = Media.empty.copy(id = aMovieId)