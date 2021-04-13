package com.raxdenstudios.app.movie.data.local.datasource

import com.raxdenstudios.app.movie.data.local.MovieDao
import com.raxdenstudios.app.movie.data.local.model.MovieEntity
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.model.APIVersion
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class MovieLocalDataSourceTest : BaseTest() {

  private val dao: MovieDao = mockk()
  private val apiDataProvider: APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      movieDataModule,
      module {
        factory(override = true) { dao }
        factory(override = true, qualifier = named(APIVersion.V3)) { apiDataProvider }
      }
    )

  private val dataSource: MovieLocalDataSource by inject()

  @Test
  fun `Given a database empty, When a 'watchList' method is called return a pageList empty`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.watchList() } returns emptyList()
      val aPage = Page(1)
      val aPageSize = PageSize.defaultSize

      val result = dataSource.watchList(aPage, aPageSize)

      assertEquals(
        PageList(emptyList<Movie>(), Page(1)),
        result
      )
    }

  @Test
  fun `Given a database with results, When a 'watchList' method is called and first page is requested return a pageList with results`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.watchList() } returns aMovies
      val aPage = Page(1)
      val aPageSize = PageSize(2)

      val result = dataSource.watchList(aPage, aPageSize)

      assertEquals(
        PageList(
          listOf(
            Movie.empty.copy(id = 1L),
            Movie.empty.copy(id = 2L),
          ), Page(1)
        ),
        result
      )
    }

  @Test
  fun `Given a database with results, When a 'watchList' method is called and second page is requested return a pageList with results`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.watchList() } returns aMovies
      val aPage = Page(2)
      val aPageSize = PageSize(2)

      val result = dataSource.watchList(aPage, aPageSize)

      assertEquals(
        PageList(
          listOf(
            Movie.empty.copy(id = 3L),
            Movie.empty.copy(id = 4L),
          ), Page(2)
        ),
        result
      )
    }

  @Test
  fun `Given a database empty, When 'isWatchList' with movieId param is called, Then return false`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.find(aMovieId) } returns MovieEntity.empty.copy(watchList = false)

      val result = dataSource.isWatchList(aMovieId)

      assertEquals(false, result)
    }

  @Test
  fun `Given a database, When 'isWatchList' with movieId param is called And this movie exists, Then return true`() = testDispatcher.runBlockingTest {
    coEvery { dao.find(aMovieId) } returns MovieEntity.empty.copy(watchList = true)

    val result = dataSource.isWatchList(aMovieId)

    assertEquals(true, result)
  }
}

private const val aMovieId = 1L
private val aMovies = listOf(
  MovieEntity.empty.copy(id = 1L),
  MovieEntity.empty.copy(id = 2L),
  MovieEntity.empty.copy(id = 3L),
  MovieEntity.empty.copy(id = 4L),
  MovieEntity.empty.copy(id = 5L),
  MovieEntity.empty.copy(id = 6L),
)