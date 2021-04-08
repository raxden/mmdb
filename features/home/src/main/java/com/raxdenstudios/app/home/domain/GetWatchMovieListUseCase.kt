package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class GetWatchMovieListUseCase(
  private val movieRepository: MovieRepository
) {

  suspend fun execute(params: Params): ResultData<PageList<Movie>> =
    movieRepository.watchList(params.page)

  data class Params(
    val page: Page = Page(1),
  )
}
