package com.raxdenstudios.app.core.data.local.datasource

import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.raxdenstudios.app.core.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.HomeModuleToEntityMapper
import com.raxdenstudios.app.core.database.HomeModuleDatabase
import com.raxdenstudios.app.core.model.HomeModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class HomeDataModuleLocalDataSourceTest {

    private lateinit var database: HomeModuleDatabase
    private val dataSource: HomeModuleLocalDataSource by lazy {
        HomeModuleLocalDataSource(
            database = database,
            homeModuleEntityToDomainMapper = HomeModuleEntityToDomainMapper(),
            homeModuleToEntityMapper = HomeModuleToEntityMapper(),
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
                val modules = awaitItem()

                assertEquals(
                    listOf(
                        HomeModule.NowPlaying.empty.copy(id = 2, order = 1),
                        HomeModule.Popular.empty.copy(id = 1, order = 2),
                        HomeModule.Watchlist.empty.copy(id = 5, order = 3),
                        HomeModule.TopRated.empty.copy(id = 3, order = 4),
                        HomeModule.Upcoming.empty.copy(id = 4, order = 5),
                    ),
                    modules
                )
            }
        }
    }
}
