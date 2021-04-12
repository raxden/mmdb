package com.raxdenstudios.app.movie.data.local.datasource

import com.raxdenstudios.app.movie.data.local.MovieWatchListDao
import com.raxdenstudios.app.movie.data.local.model.MovieWatchListEntity
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.test.BaseTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class MovieWatchListLocalDataSourceTest : BaseTest() {

  private val dao: MovieWatchListDao = mockk()

  override val modules: List<Module>
    get() = listOf(
      movieDataModule,
      module {
        factory(override = true) { dao }
      }
    )

  private val dataSource: MovieWatchListLocalDataSource by inject()
  
  @Test
  fun `Given a database empty, When 'contains' with movieId param is called, Then return false`() = testDispatcher.runBlockingTest {
    coEvery { dao.find(aMovieId) } returns null

    val result = dataSource.contains(aMovieId)

    assertEquals(false, result)
  }

  @Test
  fun `Given a database, When 'contains' with movieId param is called And this movie exists, Then return true`() = testDispatcher.runBlockingTest {
    coEvery { dao.find(aMovieId) } returns aMovieWatchListEntity

    val result = dataSource.contains(aMovieId)

    assertEquals(true, result)
  }
}

private const val aMovieId = 1L
private val aMovieWatchListEntity = MovieWatchListEntity.empty