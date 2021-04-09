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
    val carouselMoviesModel = carouselMovieListModelMapper.transform(homeModule, movieList)
    return when (homeModule) {
      HomeModule.NowPlayingMovies -> HomeModuleModel.CarouselMovies.NowPlaying(carouselMoviesModel)
      HomeModule.PopularMovies -> HomeModuleModel.CarouselMovies.Popular(carouselMoviesModel)
      HomeModule.TopRatedMovies -> HomeModuleModel.CarouselMovies.TopRated(carouselMoviesModel)
      HomeModule.UpcomingMovies -> HomeModuleModel.CarouselMovies.Upcoming(carouselMoviesModel)
      HomeModule.WatchListMovies -> when {
        carouselMoviesModel.movies.isEmpty() -> HomeModuleModel.WatchList.WithoutContent
        else -> HomeModuleModel.WatchList.WithContent(carouselMoviesModel)
      }
    }
  }
}
