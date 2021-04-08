package com.raxdenstudios.app.home.view.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class HomeModelTest {

  @Test
  fun `Given a home model with a sort of modules and a new module, When replaceModule is called, Then the same module type is replaced`() {
    val module = HomeModuleModel.CarouselMovies.NowPlaying(
      CarouselMovieListModel.empty.copy(label = "new module")
    )

    val result = aHomeModel.replaceModule(module)

    assertEquals(
      HomeModel.empty.copy(
        modules = listOf(
          HomeModuleModel.CarouselMovies.Popular(CarouselMovieListModel.empty),
          HomeModuleModel.CarouselMovies.NowPlaying(
            CarouselMovieListModel.empty.copy(label = "new module")
          ),
          HomeModuleModel.CarouselMovies.TopRated(CarouselMovieListModel.empty),
          HomeModuleModel.CarouselMovies.Upcoming(CarouselMovieListModel.empty),
        )
      ),
      result
    )
  }
}

private val aHomeModuleListModel = listOf(
  HomeModuleModel.CarouselMovies.Popular(CarouselMovieListModel.empty),
  HomeModuleModel.CarouselMovies.NowPlaying(CarouselMovieListModel.empty),
  HomeModuleModel.CarouselMovies.TopRated(CarouselMovieListModel.empty),
  HomeModuleModel.CarouselMovies.Upcoming(CarouselMovieListModel.empty),
)
private val aHomeModel = HomeModel.empty.copy(
  modules = aHomeModuleListModel
)
