package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.app.movie.domain.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class GetMoviesUseCase(
  private val movieRepository: MovieRepository
) {

  suspend fun execute(params: Params): ResultData<PageList<Movie>> = when (params) {
    is Params.BySearchType -> movieRepository.movies(params.searchType, params.page, params.pageSize)
    is Params.WatchList -> movieRepository.watchList(params.page, params.pageSize)
  }

  sealed class Params {

    abstract val page: Page
    abstract val pageSize: PageSize

    data class BySearchType(
      val searchType: SearchType,
      override val page: Page = Page(1),
      override val pageSize: PageSize = PageSize.defaultSize
    ) : Params()

    data class WatchList(
      override val page: Page = Page(1),
      override val pageSize: PageSize = PageSize.defaultSize
    ) : Params()
  }
}
