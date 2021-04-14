package com.raxdenstudios.app.movie.data.remote.datasource

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.movie.data.remote.MovieGateway
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.model.APIVersion
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.inject

@ExperimentalCoroutinesApi
internal class MovieRemoteDataSourceTest : BaseTest() {

  private val movieGateway: MovieGateway = mockk() {
    coEvery {
      watchList(aAccountLogged.credentials.accountId, any())
    } returns ResultData.Success(aPageDto)
  }
  private val apiDataProvider: APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      movieDataModule,
      module {
        factory(override = true) { movieGateway }
        factory(override = true, qualifier = named(APIVersion.V3)) { apiDataProvider }
      }
    )

  private val dataSource: MovieRemoteDataSource by inject()

  @Test
  fun `Given a searchType of type watchlist and account logged, When movies is called, Then load movies`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.movies(SearchType.WatchList, aAccountLogged, aFirstPage)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Movie.empty.copy(id = 1L, watchList = true),
              Movie.empty.copy(id = 2L, watchList = true),
            ),
            page = Page(1)
          )
        ), result
      )
    }

  @Test
  fun `Given a searchType of type watchlist and account not logged, When movies is called, Then and exception raised`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.movies(SearchType.WatchList, aAccountGuest, aFirstPage)

      result as ResultData.Error
      assert(result.throwable is UserNotLoggedException)
    }
}

private val aPageDtoList = listOf(
  MovieDto.empty.copy(id = 1),
  MovieDto.empty.copy(id = 2),
)
private val aPageDto = PageDto(
  page = 1,
  total_pages = 1,
  total_results = 2,
  results = aPageDtoList,
)
private val aFirstPage = Page(1)
private val aAccountGuest = Account.Guest.empty
private val aAccountLogged = Account.Logged.empty
