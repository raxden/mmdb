package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.CarouselMovieListModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
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
    val module = HomeModule.NowPlayingMovies

    val result = mapper.transform(module, aResultDataWithMovies)

    assertEquals(
      HomeModuleModel.CarouselMovies.NowPlaying(
        CarouselMovieListModel.empty.copy(
          movies = listOf(
            MovieListItemModel.empty.copy(id = 1L)
          )
        )
      ),
      result
    )
  }

  @Test
  fun `Given a NowPlayingMovies module and resultData without movies, When transform is called, Then return a null`() {
    val module = HomeModule.NowPlayingMovies

    val result = mapper.transform(module, aResultDataWithoutMovies)

    assertEquals(null, result)
  }

  @Test
  fun `Given a NowPlayingMovies module and resultData with error, When transform is called, Then return a null`() {
    val module = HomeModule.NowPlayingMovies
    val resultDataWithError = ResultData.Error(IllegalStateException(""))

    val result = mapper.transform(module, resultDataWithError)

    assertEquals(null, result)
  }

  @Test
  fun `Given a WatchListMovies module and resultData with a UserNotLoggedException, When transform is called, Then return a WatchListNotLogged`() {
    val module = HomeModule.WatchListMovies
    val resultDataWithError = ResultData.Error(UserNotLoggedException())

    val result = mapper.transform(module, resultDataWithError)

    assertEquals(HomeModuleModel.WatchList.NotLogged, result)
  }

  @Test
  fun `Given a WatchListMovies module and resultData with movies, When transform is called, Then return a WatchListWithContent`() {
    val module = HomeModule.WatchListMovies

    val result = mapper.transform(module, aResultDataWithMovies)

    assertEquals(
      HomeModuleModel.WatchList.WithContent(
        CarouselMovieListModel.empty.copy(
          movies = listOf(
            MovieListItemModel.empty.copy(id = 1L)
          )
        )
      ),
      result
    )
  }

  @Test
  fun `Given a WatchListMovies module and resultData without movies, When transform is called, Then return a WatchListWithoutContent`() {
    val module = HomeModule.WatchListMovies

    val result = mapper.transform(module, aResultDataWithoutMovies)

    assertEquals(HomeModuleModel.WatchList.WithoutContent, result)
  }
}

private val aResultDataWithMovies = ResultData.Success(
  PageList(
    items = listOf(
      Movie.empty.copy(id = 1L)
    ),
    page = Page(1)
  )
)
private val aResultDataWithoutMovies = ResultData.Success(
  PageList(
    items = emptyList<Movie>(),
    page = Page(1)
  )
)