package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.app.movie.domain.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class GetMoviesUseCase(
  private val movieRepository: MovieRepository
) {

  suspend fun execute(params: Params): ResultData<PageList<Movie>> = when (params) {
    is Params.BySearchType -> movieRepository.movies(params.searchType, params.page)
    is Params.WatchList -> movieRepository.watchList(params.page)
  }

  sealed class Params {

    abstract val page: Page

    data class BySearchType(
      val searchType: SearchType,
      override val page: Page = Page(1),
    ) : Params()

    data class WatchList(
      override val page: Page = Page(1),
    ) : Params()
  }
}
