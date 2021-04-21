package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.di.mediaDataModule
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
internal class MediaLocalDataSourceTest : BaseTest() {

  private val dao: WatchListDao = mockk()
  private val apiDataProvider: APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      mediaDataModule,
      module {
        factory(override = true) { dao }
        factory(override = true, qualifier = named(APIVersion.V3)) { apiDataProvider }
      }
    )

  private val dataSource: MediaLocalDataSource by inject()

  @Test
  fun `Given a media stored in local, When containsInWatchList is called, Then return true`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.find(aMovieId) } returns WatchListEntity.empty

      val result = dataSource.containsInWatchList(aMovieId)

      assertEquals(true, result)
    }
}

private const val aMovieId = 1L
