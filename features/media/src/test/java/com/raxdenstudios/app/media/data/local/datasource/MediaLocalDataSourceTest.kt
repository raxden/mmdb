package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.MediaDao
import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.model.MediaEntity
import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.di.mediaDataModule
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.model.APIVersion
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
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

  private val mediaDao: MediaDao = mockk()
  private val watchListDao: WatchListDao = mockk()
  private val apiDataProvider: APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      mediaDataModule,
      module {
        factory(override = true) { mediaDao }
        factory(override = true) { watchListDao }
        factory(
          override = true,
          qualifier = named(APIVersion.V3)
        ) { apiDataProvider }
      }
    )

  private val dataSource: MediaLocalDataSource by inject()

  @Test
  fun `Given a media, When addToWatchList is called, Then media is added`() =
    testDispatcher.runBlockingTest {
      coEvery { mediaDao.insert(aMediaEntity) } returns Unit
      coEvery { watchListDao.insert(aWatchListEntity) } returns Unit

      val result = dataSource.addToWatchList(aMedia)

      coVerifyOrder {
        mediaDao.insert(MediaEntity.empty.copy(id = aMediaId))
        watchListDao.insert(WatchListEntity(aMediaId))
      }
      assertEquals(ResultData.Success(true), result)
    }

  @Test
  fun `Given a some of mediaId's, When addToWatchList is called, Then medias are added`() =
    testDispatcher.runBlockingTest {
      coEvery { mediaDao.insert(aMediaEntityList) } returns Unit
      coEvery { watchListDao.insert(aWatchListEntityList) } returns Unit

      dataSource.addToWatchList(aMediaList)

      coVerifyOrder {
        mediaDao.insert(listOf(MediaEntity.empty.copy(id = aMediaId)))
        watchListDao.insert(listOf(WatchListEntity.empty.copy(mediaId = aMediaId)))
      }
    }

  @Test
  fun `Given a mediaId, When removeFromWatchList is called, Then media is removed`() =
    testDispatcher.runBlockingTest {
      coEvery { watchListDao.delete(aMediaId) } returns Unit

      dataSource.removeFromWatchList(aMediaId)

      coVerify { watchListDao.delete(aMediaId) }
    }

  @Test
  fun `Given a media stored in local, When containsInWatchList is called, Then return true`() =
    testDispatcher.runBlockingTest {
      coEvery { watchListDao.find(aMediaId) } returns WatchListEntity.empty

      val result = dataSource.containsInWatchList(aMediaId)

      assertEquals(true, result)
    }
}

private const val aMediaId = 1L
private val aMedia = Media.Movie.withId(aMediaId)
private val aMediaList = listOf(
  Media.Movie.withId(aMediaId),
)
private val aMediaEntity = MediaEntity.empty.copy(id = aMediaId)
private val aWatchListEntity = WatchListEntity.empty.copy(mediaId = aMediaId)
private val aMediaEntityList = listOf(
  MediaEntity.empty.copy(id = aMediaId),
)
private val aWatchListEntityList = listOf(
  WatchListEntity.empty.copy(mediaId = aMediaId),
)