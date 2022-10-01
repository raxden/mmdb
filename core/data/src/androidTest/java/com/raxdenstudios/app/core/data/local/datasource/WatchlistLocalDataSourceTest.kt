package com.raxdenstudios.app.core.data.local.datasource

import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.raxdenstudios.app.core.data.local.mapper.MediaEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.MediaToEntityMapper
import com.raxdenstudios.app.core.data.local.mapper.MediaToWatchlistEntityMapper
import com.raxdenstudios.app.core.database.MediaDatabase
import com.raxdenstudios.commons.ResultData
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class WatchlistLocalDataSourceTest {

    private lateinit var database: MediaDatabase
    private val mediaToEntityMapper: MediaToEntityMapper = mockk()
    private val mediaEntityToDomainMapper: MediaEntityToDomainMapper = mockk()
    private val mediaToWatchlistEntityMapper: MediaToWatchlistEntityMapper = mockk()
    private val dataSource: WatchlistLocalDataSource by lazy {
        WatchlistLocalDataSource(
            database = database,
            mediaToEntityMapper = mediaToEntityMapper,
            mediaEntityToDomainMapper = mediaEntityToDomainMapper,
            mediaToWatchlistEntityMapper = mediaToWatchlistEntityMapper,
        )
    }

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = MediaDatabase.switchToInMemory(context)
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun clearMoviesFromDatabase() {
        runTest {
            val result = dataSource.clear()
            assertEquals(ResultData.Success(true), result)
        }
    }
}
