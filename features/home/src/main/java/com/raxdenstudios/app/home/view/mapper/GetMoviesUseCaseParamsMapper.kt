package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.commons.util.DataMapper

internal class GetMoviesUseCaseParamsMapper : DataMapper<HomeModule, GetMoviesUseCase.Params>() {

  override fun transform(source: HomeModule): GetMoviesUseCase.Params =
    when (source) {
      HomeModule.NowPlayingMovies -> GetMoviesUseCase.Params(SearchType.NowPlaying)
      HomeModule.PopularMovies -> GetMoviesUseCase.Params(SearchType.Popular)
      HomeModule.TopRatedMovies -> GetMoviesUseCase.Params(SearchType.TopRated)
      HomeModule.UpcomingMovies -> GetMoviesUseCase.Params(SearchType.Upcoming)
      HomeModule.WatchListMovies -> GetMoviesUseCase.Params(SearchType.WatchList)
    }
}
