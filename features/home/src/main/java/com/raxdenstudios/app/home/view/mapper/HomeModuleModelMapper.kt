package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.getValueOrNull
import com.raxdenstudios.commons.pagination.model.PageList

internal class HomeModuleModelMapper(
  private val carouselMovieListModelMapper: CarouselMovieListModelMapper
) {

  fun transform(
    homeModule: HomeModule,
    movieResultData: ResultData<PageList<Movie>>
  ): HomeModuleModel {
    val movieList = movieResultData.getValueOrNull()?.items ?: emptyList()
    return when (homeModule) {
      HomeModule.NowPlayingMovies -> {
        val movieModelList = carouselMovieListModelMapper.transform(homeModule, movieList)
        HomeModuleModel.CarouselMovies.NowPlaying(movieModelList)
      }
      HomeModule.PopularMovies -> {
        val movieModelList = carouselMovieListModelMapper.transform(homeModule, movieList)
        HomeModuleModel.CarouselMovies.Popular(movieModelList)
      }
      HomeModule.TopRatedMovies -> {
        val movieModelList = carouselMovieListModelMapper.transform(homeModule, movieList)
        HomeModuleModel.CarouselMovies.TopRated(movieModelList)
      }
      HomeModule.UpcomingMovies -> {
        val movieModelList = carouselMovieListModelMapper.transform(homeModule, movieList)
        HomeModuleModel.CarouselMovies.Upcoming(movieModelList)
      }
    }
  }
}
