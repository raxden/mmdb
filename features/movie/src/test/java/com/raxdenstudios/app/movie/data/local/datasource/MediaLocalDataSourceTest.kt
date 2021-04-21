package com.raxdenstudios.app.movie.data.local.datasource

import com.raxdenstudios.app.movie.data.local.MediaDao
import com.raxdenstudios.app.movie.data.local.model.MediaEntity
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.movie.domain.model.Media
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
internal class MediaLocalDataSourceTest : BaseTest() {

  private val dao: MediaDao = mockk()
  private val apiDataProvider: APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      movieDataModule,
      module {
        factory(override = true) { dao }
        factory(override = true, qualifier = named(APIVersion.V3)) { apiDataProvider }
      }
    )

  private val dataSource: MediaLocalDataSource by inject()

  @Test
  fun `Given a database empty, When a 'watchList' method is called return a pageList empty`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.watchList() } returns emptyList()
      val aPage = Page(1)
      val aPageSize = PageSize.defaultSize

      val result = dataSource.watchList(aPage, aPageSize)

      assertEquals(
        PageList(emptyList<Media>(), Page(1)),
        result
      )
    }

  @Test
  fun `Given a database with results, When a 'watchList' method is called requesting first page, Then a first page with results is returned`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.watchList() } returns aMovies
      val aPage = Page(1)
      val aPageSize = PageSize(2)

      val result = dataSource.watchList(aPage, aPageSize)

      assertEquals(
        PageList(
          listOf(
            Media.empty.copy(id = 1L),
            Media.empty.copy(id = 2L),
          ), Page(1)
        ),
        result
      )
    }

  @Test
  fun `Given a database with results, When a 'watchList' method is called requesting second page, Then a second page with results is returned`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.watchList() } returns aMovies
      val aPage = Page(2)
      val aPageSize = PageSize(2)

      val result = dataSource.watchList(aPage, aPageSize)

      assertEquals(
        PageList(
          listOf(
            Media.empty.copy(id = 3L),
            Media.empty.copy(id = 4L),
          ), Page(2)
        ),
        result
      )
    }

  @Test
  fun `Given a database empty, When 'isWatchList' with movieId param is called, Then return false`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.find(aMovieId) } returns MediaEntity.empty.copy(watchList = false)

      val result = dataSource.isWatchList(aMovieId)

      assertEquals(false, result)
    }

  @Test
  fun `Given a database, When 'isWatchList' with movieId param is called And this movie exists, Then return true`() = testDispatcher.runBlockingTest {
    coEvery { dao.find(aMovieId) } returns MediaEntity.empty.copy(watchList = true)

    val result = dataSource.isWatchList(aMovieId)

    assertEquals(true, result)
  }
}

private const val aMovieId = 1L
private val aMovies = listOf(
  MediaEntity.empty.copy(id = 1L),
  MediaEntity.empty.copy(id = 2L),
  MediaEntity.empty.copy(id = 3L),
  MediaEntity.empty.copy(id = 4L),
  MediaEntity.empty.copy(id = 5L),
  MediaEntity.empty.copy(id = 6L),
)