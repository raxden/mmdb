package com.raxdenstudios.app.home.data.local.datasource

import com.raxdenstudios.app.home.data.local.HomeModuleDao
import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.test.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class HomeModuleLocalDataSourceTest : BaseTest() {

  private val dao: HomeModuleDao = mockk {
    coEvery { insert(any()) } returns Unit
  }

  override val modules: List<Module>
    get() = listOf(
      homeFeatureModule,
      module {
        factory(override = true) { dao }
      }
    )

  private val dataSource: HomeModuleLocalDataSource by inject()

  @Test
  fun `Given an empty database, When modules is called, Then default modules are inserted in database`() {
    testDispatcher.runBlockingTest {

      coEvery { dao.observeAll() } returns flow { emit(emptyList<HomeModuleEntity>()) }

      val flow = dataSource.observe()

      flow.collect { modules ->
        assertEquals(emptyList<HomeModuleEntity>(), modules)
      }

      coVerify {
        dao.insert(
          listOf(
            HomeModuleEntity.popular,
            HomeModuleEntity.nowPlaying,
            HomeModuleEntity.topRated,
            HomeModuleEntity.upcoming,
            HomeModuleEntity.watchList,
          )
        )
      }
    }
  }

  @Test
  fun `Given an fill database, When modules is called, Then modules are returned`() {
    testDispatcher.runBlockingTest {

      coEvery { dao.observeAll() } returns flow { emit(aHomeModuleEntityList) }

      val flow = dataSource.observe()

      flow.collect { modules ->
        assertEquals(
          listOf(
            HomeModule.PopularMovies,
            HomeModule.NowPlayingMovies,
            HomeModule.TopRatedMovies,
            HomeModule.UpcomingMovies,
          ), modules
        )
      }

      coVerify(exactly = 0) {
        dao.insert(any())
      }
    }
  }
}

private val aHomeModuleEntityList = listOf(
  HomeModuleEntity.popular,
  HomeModuleEntity.nowPlaying,
  HomeModuleEntity.topRated,
  HomeModuleEntity.upcoming,
)
