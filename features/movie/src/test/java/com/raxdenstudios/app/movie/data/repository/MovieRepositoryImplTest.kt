package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.movie.data.local.datasource.MovieLocalDataSource
import com.raxdenstudios.app.movie.data.remote.datasource.MovieRemoteDataSource
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
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
internal class MovieRepositoryImplTest : BaseTest() {

  private val accountLocalDataSource: AccountLocalDataSource = mockk {
    coEvery { getAccount() } returns aAccountLogged
  }
  private val movieRemoteDataSource: MovieRemoteDataSource = mockk {
    coEvery {
      addMovieToWatchList(aCredentialsAccountId, aMovieId)
    } returns ResultData.Success(true)
    coEvery {
      removeMovieFromWatchList(aCredentialsAccountId, aMovieId)
    } returns ResultData.Success(true)
    coEvery {
      movies(any(), aPage)
    } returns ResultData.Success(aPageList)
    coEvery {
      watchList(aCredentialsAccountId, aPage)
    } returns ResultData.Success(aPageList)
    coEvery {
      movieById(aMovieId)
    } returns ResultData.Success(Movie.empty.copy(id = aMovieId))
  }
  private val movieLocalDataSource: MovieLocalDataSource = mockk {
    coEvery { insert(any<Movie>()) } returns Unit
    coEvery { isWatchList(any()) } returns false
  }
  private val apiDataProvider: APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      movieDataModule,
      module {
        factory(override = true) { accountLocalDataSource }
        factory(override = true) { movieRemoteDataSource }
        factory(override = true) { movieLocalDataSource }
        factory(override = true) { apiDataProvider }
      }
    )

  private val repository: MovieRepository by inject()

  @Test
  fun `Given a movie, When addMovieToWatchList is called, Then movie is added`() =
    testDispatcher.runBlockingTest {
      val result = repository.addMovieToWatchList(aMovieId)

      coVerify { movieLocalDataSource.insert(aMovie.copy(watchList = true)) }
      assertEquals(ResultData.Success(true), result)
    }

  @Test
  fun `Given a movie, When removeMovieFromWatchList is called, Then movie is removed`() = testDispatcher.runBlockingTest {
    val result = repository.removeMovieFromWatchList(aMovieId)

    coVerify { movieLocalDataSource.insert(aMovie.copy(watchList = false)) }
    assertEquals(ResultData.Success(true), result)
  }

  @Test
  fun `Given a movies returned by the server, When movies are called, Then movies returned should be marked as watched if requires`() =
    testDispatcher.runBlockingTest {
      coEvery { movieLocalDataSource.isWatchList(2L) } returns true

      val result = repository.movies(SearchType.Popular, aPage, aPageSize)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Movie.empty.copy(id = 1L),
              Movie.empty.copy(id = 2L, watchList = true),
            ),
            page = Page(1),
          )
        ),
        result
      )
    }
}

private val aMovies = listOf(
  Movie.empty.copy(id = 1L),
  Movie.empty.copy(id = 2L),
)
private val aPage = Page(1)
private val aPageSize = PageSize.defaultSize
private val aPageList = PageList(
  items = aMovies,
  page = aPage,
)
private const val aCredentialsAccountId = "aCredentialsAccountId"
private val aAccountLogged = Account.Logged.empty.copy(
  credentials = Credentials.empty.copy(
    accountId = aCredentialsAccountId
  )
)
private const val aMovieId = 1L
private val aMovie = Movie.empty.copy(id = aMovieId)