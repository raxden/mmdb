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

  suspend fun execute(params: Params): ResultData<PageList<Movie>> =
    movieRepository.movies(params.searchType, params.page)

  data class Params(
    val searchType: SearchType,
    val page: Page = Page(1),
  )
}
