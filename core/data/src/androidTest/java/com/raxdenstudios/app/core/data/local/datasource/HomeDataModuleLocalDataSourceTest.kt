package com.raxdenstudios.app.core.data.local.datasource

import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.raxdenstudios.app.core.data.local.mapper.ExceptionToErrorMapper
import com.raxdenstudios.app.core.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.HomeModuleToEntityMapper
import com.raxdenstudios.app.core.database.HomeModuleDatabase
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@SmallTest
class HomeDataModuleLocalDataSourceTest {

    private lateinit var database: HomeModuleDatabase
    private val dataSource: HomeModuleLocalDataSource by lazy {
        HomeModuleLocalDataSource(
            database = database,
            homeModuleEntityToDomainMapper = HomeModuleEntityToDomainMapper(),
            homeModuleToEntityMapper = HomeModuleToEntityMapper(),
            exceptionToErrorMapper = ExceptionToErrorMapper()
        )
    }

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = HomeModuleDatabase.switchToInMemory(context)
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun given_an_empty_database_When_modules_is_called_Then_default_modules_are_inserted_in_database() {
        runTest {
            dataSource.observe().test {
                val result = awaitItem()
                assertEquals(ResultData.Success(emptyList<HomeModule>()), result)

                val result2 = awaitItem()
                assertEquals(
                    ResultData.Success(
                        listOf(
                            HomeModule.Carousel.nowPlaying(id = 2, order = 1),
                            HomeModule.Carousel.popular(
                                id = 1, order = 2, mediaType = MediaType.Movie
                            ),
                            HomeModule.Carousel.watchlist(
                                id = 5, order = 3, mediaType = MediaType.Movie
                            ),
                            HomeModule.Carousel.topRated(
                                id = 3, order = 4, mediaType = MediaType.Movie
                            ),
                            HomeModule.Carousel.upcoming(id = 4, order = 5),
                        )
                    ), result2
                )
            }
        }
    }
}
