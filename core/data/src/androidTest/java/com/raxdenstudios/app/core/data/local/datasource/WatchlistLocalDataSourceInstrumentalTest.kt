package com.raxdenstudios.app.core.data.local.datasource

import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.raxdenstudios.app.core.data.local.mapper.ExceptionToErrorMapper
import com.raxdenstudios.app.core.data.local.mapper.MediaEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.MediaToEntityMapper
import com.raxdenstudios.app.core.data.local.mapper.MediaToWatchlistEntityMapper
import com.raxdenstudios.app.core.data.local.mapper.PictureEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.PictureToEntityMapper
import com.raxdenstudios.app.core.data.local.mapper.SizeEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.SizeToEntityMapper
import com.raxdenstudios.app.core.data.local.mapper.VoteEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.VoteToEntityMapper
import com.raxdenstudios.app.core.database.MediaDatabase
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.commons.ResultData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class WatchlistLocalDataSourceInstrumentalTest {

    private lateinit var database: MediaDatabase
    private val sizeToEntityMapper: SizeToEntityMapper = SizeToEntityMapper()
    private val pictureToEntityMapper: PictureToEntityMapper = PictureToEntityMapper(sizeToEntityMapper)
    private val voteToEntityMapper: VoteToEntityMapper = VoteToEntityMapper()
    private val mediaToEntityMapper: MediaToEntityMapper = MediaToEntityMapper(
        pictureToEntityMapper = pictureToEntityMapper,
        voteToEntityMapper = voteToEntityMapper
    )
    private val sizeEntityToDomainMapper: SizeEntityToDomainMapper = SizeEntityToDomainMapper()
    private val pictureEntityToDomainMapper: PictureEntityToDomainMapper = PictureEntityToDomainMapper(
        sizeEntityToDomainMapper = sizeEntityToDomainMapper
    )
    private val voteEntityToDomainMapper: VoteEntityToDomainMapper = VoteEntityToDomainMapper()
    private val mediaEntityToDomainMapper: MediaEntityToDomainMapper = MediaEntityToDomainMapper(
        pictureEntityToDomainMapper = pictureEntityToDomainMapper,
        voteEntityToDomainMapper = voteEntityToDomainMapper
    )
    private val mediaToWatchlistEntityMapper: MediaToWatchlistEntityMapper = MediaToWatchlistEntityMapper()
    private val exceptionToErrorMapper: ExceptionToErrorMapper = ExceptionToErrorMapper()
    private val dataSource: WatchlistLocalDataSource by lazy {
        WatchlistLocalDataSource(
            database = database,
            mediaToEntityMapper = mediaToEntityMapper,
            mediaEntityToDomainMapper = mediaEntityToDomainMapper,
            mediaToWatchlistEntityMapper = mediaToWatchlistEntityMapper,
            exceptionToErrorMapper = exceptionToErrorMapper
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

    @Test
    fun returnErrorWhenMediaIsNotFound() {
        runTest {
            val mediaId = MediaId(1)
            dataSource.observe(mediaId).test {
                val result = awaitItem()
                assertTrue(result is ResultData.Failure)
            }
        }
    }
}
