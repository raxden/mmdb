package com.raxdenstudios.app.home.data.local.datasource

import com.raxdenstudios.app.home.data.local.HomeModuleDao
import com.raxdenstudios.app.home.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.test.BasePresentationTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
internal class HomeModuleLocalDataSourceTest : BasePresentationTest() {

    private val homeModuleEntityToDomainMapper = HomeModuleEntityToDomainMapper()
    private val homeModuleDao: HomeModuleDao = mockk {
        coEvery { insert(any()) } returns Unit
    }
    private val dataSource: HomeModuleLocalDataSource by lazy {
        HomeModuleLocalDataSource(
            homeModuleDao = homeModuleDao,
            homeModuleEntityToDomainMapper = homeModuleEntityToDomainMapper,
        )
    }

    @Test
    fun `Given an empty database, When modules is called, Then default modules are inserted in database`() {
        runTest {

            coEvery { homeModuleDao.observeAll() } returns flow { emit(emptyList<HomeModuleEntity>()) }

            val flow = dataSource.observe()

            flow.collect { modules ->
                assertEquals(emptyList<HomeModuleEntity>(), modules)
            }

            coVerify {
                homeModuleDao.insert(
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
        runTest {

            coEvery { homeModuleDao.observeAll() } returns flow { emit(aHomeModuleEntityList) }

            val flow = dataSource.observe()

            flow.collect { modules ->
                assertEquals(
                    listOf(
                        HomeModule.popularMovies,
                        HomeModule.nowPlayingMovies,
                        HomeModule.topRatedMovies,
                        HomeModule.upcomingMovies,
                    ), modules
                )
            }

            coVerify(exactly = 0) {
                homeModuleDao.insert(any())
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
