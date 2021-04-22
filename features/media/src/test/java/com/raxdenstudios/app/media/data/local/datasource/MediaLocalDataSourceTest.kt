package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.di.mediaDataModule
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.model.APIVersion
import com.raxdenstudios.app.test.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
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
  fun `Given a mediaId, When addToWatchList is called, Then media is added`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.insert(aWatchListEntity) } returns Unit

      dataSource.addToWatchList(aMedia)

      coVerify { dao.insert(WatchListEntity(aMediaId)) }
    }

  @Test
  fun `Given a some of mediaId's, When addToWatchList is called, Then medias are added`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.insert(aWatchListEntityList) } returns Unit

      dataSource.addToWatchList(aMediaList)

      coVerify {
        dao.insert(
          listOf(
            WatchListEntity.empty.copy(mediaId = aMediaId),
          )
        )
      }
    }

  @Test
  fun `Given a mediaId, When removeFromWatchList is called, Then media is removed`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.remove(aMediaId) } returns Unit

      dataSource.removeFromWatchList(aMediaId)

      coVerify { dao.remove(aMediaId) }
    }

  @Test
  fun `Given a media stored in local, When containsInWatchList is called, Then return true`() =
    testDispatcher.runBlockingTest {
      coEvery { dao.find(aMediaId) } returns WatchListEntity.empty

      val result = dataSource.containsInWatchList(aMediaId)

      assertEquals(true, result)
    }
}

private const val aMediaId = 1L
private val aMediaIds = listOf(1L)
private val aMedia = Media.withId(aMediaId)
private val aMediaList = listOf(
  Media.withId(aMediaId),
)
private val aWatchListEntity = WatchListEntity.empty.copy(mediaId = aMediaId)
private val aWatchListEntityList = listOf(
  WatchListEntity.empty.copy(mediaId = aMediaId),
)