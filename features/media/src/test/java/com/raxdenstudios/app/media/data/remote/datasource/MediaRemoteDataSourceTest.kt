package com.raxdenstudios.app.media.data.remote.datasource

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.media.data.remote.MediaGateway
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.di.mediaDataModule
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.inject

@ExperimentalCoroutinesApi
internal class MediaRemoteDataSourceTest : BaseTest() {

  private val mediaGateway: MediaGateway = mockk() {
    coEvery { upcoming(any()) } returns ResultData.Success(aPageDto)
    coEvery { topRated("movie", any()) } returns ResultData.Success(aPageDto)
    coEvery { popular("movie", any()) } returns ResultData.Success(aPageDto)
    coEvery { nowPlaying("movie", any()) } returns ResultData.Success(aPageDto)
    coEvery {
      watchList(aAccountLogged.credentials.accountId, "movie", any())
    } returns ResultData.Success(aPageDto)
    coEvery {
      watchList(aAccountLogged.credentials.accountId, "movie")
    } returns ResultData.Success(aPageDtoList)
    coEvery {
      detail(aMediaId.toString(), "movie")
    } returns ResultData.Success(aMediaDto)
    coEvery {
      addToWatchList(aTMDBAccountId, "movie", aMediaId)
    } returns ResultData.Success(true)
    coEvery {
      removeFromWatchList(aTMDBAccountId, "movie", aMediaId)
    } returns ResultData.Success(true)
  }
  private val apiDataProvider: com.raxdenstudios.app.network.APIDataProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      mediaDataModule,
      module {
        factory(override = true) { mediaGateway }
        factory(
          override = true,
          qualifier = named(com.raxdenstudios.app.network.model.APIVersion.V3)
        ) { apiDataProvider }
      }
    )

  private val dataSource: MediaRemoteDataSource by inject()

  @Test
  fun `Given a mediaId and mediaType, When mediaById is called, Then returns a ResultData success with Media`() =
    testDispatcher.runBlockingTest {
      val result = dataSource.mediaById(aMediaId, aMediaType)

      assertEquals(
        ResultData.Success(Media.empty.copy(id = 1L, mediaType = MediaType.Movie)),
        result
      )
    }

  @Test
  fun `Given a account logged, mediaId and mediaType, When addMediaToWatchList is called, Then returns a ResultData success with Media`() =
    testDispatcher.runBlockingTest {
      val result = dataSource.addMediaToWatchList(aAccountLogged, aMediaType, aMediaId)

      assertEquals(
        ResultData.Success(
          Media.empty.copy(
            id = 1L,
            watchList = true,
            mediaType = MediaType.Movie
          )
        ),
        result
      )
    }

  @Test
  fun `Given a account logged, mediaId and mediaType, When removeMediaFromWatchList is called, Then returns a ResultData success`() =
    testDispatcher.runBlockingTest {
      val result = dataSource.removeMediaFromWatchList(aAccountLogged, aMediaType, aMediaId)

      assertEquals(
        ResultData.Success(true),
        result
      )
    }

  @Test
  fun `Given a account logged and mediaType, When watchList is called, Then returns a List of Media's`() =
    testDispatcher.runBlockingTest {
      val result = dataSource.watchList(aAccountLogged, aMediaType)

      assertEquals(
        ResultData.Success(
          listOf(
            Media.empty.copy(id = 1L, watchList = true),
            Media.empty.copy(id = 2L, watchList = true)
          )
        ),
        result
      )
    }

  @Test
  fun `Given a mediaFilter of type upcoming, When movies is called, Then load movies`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.medias(MediaFilter.Upcoming, aAccountLogged, aFirstPage)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Media.empty.copy(id = 1L),
              Media.empty.copy(id = 2L),
            ),
            page = Page(1)
          )
        ), result
      )
    }

  @Test
  fun `Given a mediaFilter of type popular, When movies is called, Then load movies`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.medias(MediaFilter.popularMovies, aAccountLogged, aFirstPage)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Media.empty.copy(id = 1L),
              Media.empty.copy(id = 2L),
            ),
            page = Page(1)
          )
        ), result
      )
    }

  @Test
  fun `Given a mediaFilter of type topRated, When movies is called, Then load movies`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.medias(MediaFilter.topRatedMovies, aAccountLogged, aFirstPage)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Media.empty.copy(id = 1L),
              Media.empty.copy(id = 2L),
            ),
            page = Page(1)
          )
        ), result
      )
    }

  @Test
  fun `Given a mediaFilter of type nowPlaying, When movies is called, Then load movies`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.medias(MediaFilter.nowPlayingMovies, aAccountLogged, aFirstPage)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Media.empty.copy(id = 1L),
              Media.empty.copy(id = 2L),
            ),
            page = Page(1)
          )
        ), result
      )
    }

  @Test
  fun `Given a mediaFilter of type watchlist and account logged, When movies is called, Then load movies`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.medias(MediaFilter.watchListMovies, aAccountLogged, aFirstPage)

      assertEquals(
        ResultData.Success(
          PageList(
            items = listOf(
              Media.empty.copy(id = 1L, watchList = true),
              Media.empty.copy(id = 2L, watchList = true),
            ),
            page = Page(1)
          )
        ), result
      )
    }

  @Test
  fun `Given a mediaFilter of type watchlist and account not logged, When movies is called, Then UserNotLoggedException is returned`() =
    testDispatcher.runBlockingTest {

      val result = dataSource.medias(MediaFilter.watchListMovies, aAccountGuest, aFirstPage)

      result as ResultData.Error
      assert(result.throwable is UserNotLoggedException)
    }
}

private const val aTMDBAccountId = "aTMDBAccountId"
private const val aMediaId = 1L
private val aMediaDto = MediaDto.empty.copy(id = 1)
private val aMediaType = MediaType.Movie
private val aPageDtoList = listOf(
  MediaDto.empty.copy(id = 1),
  MediaDto.empty.copy(id = 2),
)
private val aPageDto = com.raxdenstudios.app.network.model.PageDto(
  page = 1,
  total_pages = 1,
  total_results = 2,
  results = aPageDtoList,
)
private val aFirstPage = Page(1)
private val aAccountGuest = Account.Guest.empty
private val aAccountLogged =
  Account.Logged.empty.copy(credentials = Credentials.empty.copy(accountId = aTMDBAccountId))