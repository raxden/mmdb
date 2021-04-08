package com.raxdenstudios.app.home.domain.model

sealed class HomeModule {

  object PopularMovies : HomeModule()
  object NowPlayingMovies : HomeModule()
  object TopRatedMovies : HomeModule()
  object UpcomingMovies : HomeModule()
}
