package com.raxdenstudios.app.movie.domain.model

sealed class SearchType {

  object Popular : SearchType()
  object NowPlaying : SearchType()
  object TopRated : SearchType()
  object Upcoming : SearchType()
  object WatchList : SearchType()
}
