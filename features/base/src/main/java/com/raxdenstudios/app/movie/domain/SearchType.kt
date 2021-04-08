package com.raxdenstudios.app.movie.domain

sealed class SearchType {

  object Popular : SearchType()
  object NowPlaying : SearchType()
  object TopRated : SearchType()
  object Upcoming : SearchType()
}
