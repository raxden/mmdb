package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.getValueOrNull
import com.raxdenstudios.commons.pagination.model.PageList

internal class HomeModuleModelMapper(
  private val carouselMovieListModelMapper: CarouselMovieListModelMapper
) {

  fun transform(
    homeModule: HomeModule,
    resultData: ResultData<PageList<Movie>>
  ): HomeModuleModel? {
    val movieList = resultData.getValueOrNull()?.items ?: emptyList()
    val carouselMoviesModel = carouselMovieListModelMapper.transform(homeModule, movieList)
    val hasContent = carouselMoviesModel.hasMovies()
    return when (homeModule) {
      HomeModule.NowPlayingMovies -> when {
        hasContent -> HomeModuleModel.CarouselMovies.NowPlaying(carouselMoviesModel)
        else -> null
      }
      HomeModule.PopularMovies -> when {
        hasContent -> HomeModuleModel.CarouselMovies.Popular(carouselMoviesModel)
        else -> null
      }
      HomeModule.TopRatedMovies -> when {
        hasContent -> HomeModuleModel.CarouselMovies.TopRated(carouselMoviesModel)
        else -> null
      }
      HomeModule.UpcomingMovies -> when {
        hasContent -> HomeModuleModel.CarouselMovies.Upcoming(carouselMoviesModel)
        else -> null
      }
      HomeModule.WatchListMovies -> when (resultData) {
        is ResultData.Error -> {
          if (resultData.throwable is UserNotLoggedException) HomeModuleModel.WatchList.NotLogged
          else null
        }
        is ResultData.Success -> when {
          hasContent -> HomeModuleModel.WatchList.WithContent(carouselMoviesModel)
          else -> HomeModuleModel.WatchList.WithoutContent
        }
      }
    }
  }
}
