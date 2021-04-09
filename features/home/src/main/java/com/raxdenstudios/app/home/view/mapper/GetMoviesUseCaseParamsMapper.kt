package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.movie.domain.SearchType
import com.raxdenstudios.commons.util.DataMapper

internal class GetMoviesUseCaseParamsMapper : DataMapper<HomeModule, GetMoviesUseCase.Params>() {

  override fun transform(source: HomeModule): GetMoviesUseCase.Params =
    when (source) {
      HomeModule.NowPlayingMovies -> GetMoviesUseCase.Params.BySearchType(SearchType.NowPlaying)
      HomeModule.PopularMovies -> GetMoviesUseCase.Params.BySearchType(SearchType.Popular)
      HomeModule.TopRatedMovies -> GetMoviesUseCase.Params.BySearchType(SearchType.TopRated)
      HomeModule.UpcomingMovies -> GetMoviesUseCase.Params.BySearchType(SearchType.Upcoming)
      HomeModule.WatchListMovies -> GetMoviesUseCase.Params.WatchList()
    }
}
