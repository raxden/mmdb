package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.view.model.MediaFilterModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class HomeModuleModelMapperTest : BaseTest() {

  private val stringProvider: StringProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      baseFeatureModule,
      homeFeatureModule,
      module {
        factory(override = true) { stringProvider }
      }
    )

  private val mapper: HomeModuleModelMapper by inject()

  @Test
  fun `Given a NowPlayingMovies module and resultData with movies, When transform is called, Then return a NowPlayingMoviesModel`() {
    val module = HomeModule.nowPlayingMovies

    val result = mapper.transform(module, aResultDataWithMovies)

    assertEquals(
      HomeModuleModel.CarouselMedias(
        mediaFilterModel = MediaFilterModel.nowPlayingMovies,
        carouselMediaListModel = CarouselMediaListModel.empty.copy(
          medias = listOf(
            MediaListItemModel.empty.copy(id = 1L)
          )
        )
      ),
      result
    )
  }

  @Test
  fun `Given a NowPlayingMovies module and resultData without movies, When transform is called, Then return a null`() {
    val module = HomeModule.nowPlayingMovies

    val result = mapper.transform(module, aResultDataWithoutMovies)

    assertEquals(null, result)
  }

  @Test
  fun `Given a NowPlayingMovies module and resultData with error, When transform is called, Then return a null`() {
    val module = HomeModule.nowPlayingMovies
    val resultDataWithError = ResultData.Error(IllegalStateException(""))

    val result = mapper.transform(module, resultDataWithError)

    assertEquals(null, result)
  }

  @Test
  fun `Given a WatchListMovies module and resultData with a UserNotLoggedException, When transform is called, Then return a WatchListNotLogged`() {
    val module = HomeModule.watchListMovies
    val resultDataWithError = ResultData.Error(UserNotLoggedException())

    val result = mapper.transform(module, resultDataWithError)

    assertEquals(HomeModuleModel.WatchlistNotLogged, result)
  }

  @Test
  fun `Given a WatchListMovies module and resultData with movies, When transform is called, Then return a WatchListWithContent`() {
    val module = HomeModule.watchListMovies

    val result = mapper.transform(module, aResultDataWithMovies)

    assertEquals(
      HomeModuleModel.CarouselMedias(
        mediaFilterModel = MediaFilterModel.watchlistMovies,
        carouselMediaListModel = CarouselMediaListModel.empty.copy(
          medias = listOf(
            MediaListItemModel.empty.copy(id = 1L)
          )
        )
      ),
      result
    )
  }

  @Test
  fun `Given a WatchListMovies module and resultData without movies, When transform is called, Then return a WatchListWithoutContent`() {
    val module = HomeModule.watchListMovies

    val result = mapper.transform(module, aResultDataWithoutMovies)

    assertEquals(HomeModuleModel.WatchlistWithoutContent, result)
  }
}

private val aResultDataWithMovies = ResultData.Success(
  PageList<Media>(
    items = listOf(
      Media.Movie.empty.copy(id = 1L)
    ),
    page = Page(1)
  )
)
private val aResultDataWithoutMovies = ResultData.Success(
  PageList(
    items = emptyList<Media>(),
    page = Page(1)
  )
)