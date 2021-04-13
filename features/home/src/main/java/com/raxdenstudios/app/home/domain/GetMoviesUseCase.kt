package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class GetMoviesUseCase(
  private val movieRepository: MovieRepository
) {

  suspend fun execute(params: Params): ResultData<PageList<Movie>> =
    movieRepository.movies(params.searchType, params.page, params.pageSize)

  data class Params(
    val searchType: SearchType,
    val page: Page = Page(1),
    val pageSize: PageSize = PageSize.defaultSize
  )
}
