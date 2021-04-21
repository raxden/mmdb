package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.app.movie.view.model.MediaTypeModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.getValueOrNull
import com.raxdenstudios.commons.pagination.model.PageList

internal class HomeModuleModelMapper(
  private val carouselMediaListModelMapper: CarouselMediaListModelMapper
) {

  fun transform(
    homeModule: HomeModule,
    resultData: ResultData<PageList<Media>>
  ): HomeModuleModel? {
    val movieList = resultData.getValueOrNull()?.items ?: emptyList()
    val carouselMoviesModel = carouselMediaListModelMapper.transform(homeModule, movieList)
    val hasContent = carouselMoviesModel.hasMovies()
    return when (homeModule) {
      is HomeModule.NowPlaying -> when {
        hasContent -> HomeModuleModel.CarouselMedias(
          mediaFilterModel = MediaFilterModel.NowPlaying(MediaTypeModel.Movie),
          carouselMediaListModel = carouselMoviesModel
        )
        else -> null
      }
      is HomeModule.Popular -> when {
        hasContent -> HomeModuleModel.CarouselMedias(
          mediaFilterModel = MediaFilterModel.Popular(MediaTypeModel.Movie),
          carouselMediaListModel = carouselMoviesModel
        )
        else -> null
      }
      is HomeModule.TopRated -> when {
        hasContent -> HomeModuleModel.CarouselMedias(
          mediaFilterModel = MediaFilterModel.TopRated(MediaTypeModel.Movie),
          carouselMediaListModel = carouselMoviesModel
        )
        else -> null
      }
      is HomeModule.Upcoming -> when {
        hasContent -> HomeModuleModel.CarouselMedias(
          mediaFilterModel = MediaFilterModel.Upcoming,
          carouselMediaListModel = carouselMoviesModel
        )
        else -> null
      }
      is HomeModule.WatchList -> when (resultData) {
        is ResultData.Error -> {
          if (resultData.throwable is UserNotLoggedException) HomeModuleModel.WatchlistNotLogged
          else null
        }
        is ResultData.Success -> when {
          hasContent -> HomeModuleModel.CarouselMedias(
            mediaFilterModel = MediaFilterModel.WatchList(MediaTypeModel.Movie),
            carouselMediaListModel = carouselMoviesModel
          )
          else -> HomeModuleModel.WatchlistWithoutContent
        }
      }
    }
  }
}
