package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.PageList

internal class GetMoviesUseCaseImpl(
  private val movieRepository: MovieRepository
) : GetMoviesUseCase {

  override suspend fun execute(params: GetMoviesUseCase.Params): ResultData<PageList<Movie>> =
    movieRepository.movies(params.searchType, params.page, params.pageSize)
}