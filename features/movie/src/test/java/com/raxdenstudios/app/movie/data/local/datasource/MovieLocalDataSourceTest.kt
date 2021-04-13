package com.raxdenstudios.app.movie.data.local.datasource

import com.raxdenstudios.app.movie.data.local.MovieDao
import com.raxdenstudios.app.movie.data.local.model.MovieEntity
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.model.APIVersion
import com.raxdenstudios.app.test.BaseTest
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
  fun `Given a database empty, When 'isWatchList' with movieId param is called, Then return false`() = testDispatcher.runBlockingTest {
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