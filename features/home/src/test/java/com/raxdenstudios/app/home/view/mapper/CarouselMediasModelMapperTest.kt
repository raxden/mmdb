package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class CarouselMediasModelMapperTest : BaseTest() {

  private val stringProvider: StringProvider = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      baseFeatureModule,
      homeFeatureModule,
      module {
        factory(override = true) { stringProvider }
      }
    )

  private val mapper: CarouselMediasModelMapper by inject()

  @Test
  fun `Given a NowPlaying module and resultData with movies, When transform is called, Then return a NowPlaying carousel`() {
    val module = HomeModule.nowPlayingMovies

    val result = mapper.transform(module, false, aMedias)

    assertEquals(
      HomeModuleModel.CarouselMedias.NowPlaying.empty.copy(
        medias = listOf(MediaListItemModel.empty.copy(id = 1L))
      ),
      result
    )
  }

  @Test
  fun `Given a Popular module and resultData with movies, When transform is called, Then return a Popular carousel`() {
    val module = HomeModule.popularMovies

    val result = mapper.transform(module, false, aMedias)

    assertEquals(
      HomeModuleModel.CarouselMedias.Popular.empty.copy(
        medias = listOf(MediaListItemModel.empty.copy(id = 1L))
      ),
      result
    )
  }

  @Test
  fun `Given a TopRated module and resultData with movies, When transform is called, Then return a TopRated carousel`() {
    val module = HomeModule.topRatedMovies

    val result = mapper.transform(module, false, aMedias)

    assertEquals(
      HomeModuleModel.CarouselMedias.TopRated.empty.copy(
        medias = listOf(MediaListItemModel.empty.copy(id = 1L))
      ),
      result
    )
  }

  @Test
  fun `Given a Upcoming module and resultData with movies, When transform is called, Then return a Upcoming carousel`() {
    val module = HomeModule.upcomingMovies

    val result = mapper.transform(module, false, aMedias)

    assertEquals(
      HomeModuleModel.CarouselMedias.Upcoming.empty.copy(
        medias = listOf(MediaListItemModel.empty.copy(id = 1L))
      ),
      result
    )
  }

  @Test
  fun `Given a WatchList module and resultData with movies, When transform is called, Then return a WatchList carousel`() {
    val module = HomeModule.watchListMovies

    val result = mapper.transform(module, false, aMedias)

    assertEquals(
      HomeModuleModel.CarouselMedias.WatchList.empty.copy(
        medias = listOf(MediaListItemModel.empty.copy(id = 1L)),
        requireSigIn = true,
      ),
      result
    )
  }
}

private val aMedias = listOf(
  Media.Movie.empty.copy(id = 1L)
)
