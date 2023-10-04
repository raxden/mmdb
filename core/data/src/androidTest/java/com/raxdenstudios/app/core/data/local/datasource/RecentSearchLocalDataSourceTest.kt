package com.raxdenstudios.app.core.data.local.datasource

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.raxdenstudios.app.core.data.local.mapper.ExceptionToErrorMapper
import com.raxdenstudios.app.core.database.RecentSearchDatabase
import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecentSearchLocalDataSourceTest {

    private lateinit var database: RecentSearchDatabase
    private lateinit var dataSource: RecentSearchLocalDataSource

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = RecentSearchDatabase.switchToInMemory(context)
        dataSource = RecentSearchLocalDataSource(
            database = database,
            exceptionToErrorMapper = ExceptionToErrorMapper(),
        )
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun observe_should_return_a_flow_of_result_data() = runTest {
        dataSource.add("query")
        dataSource.observe().test {
            val result = awaitItem()
            assertEquals(ResultData.Success(listOf("query")), result)

            cancelAndConsumeRemainingEvents()
        }
    }
}
