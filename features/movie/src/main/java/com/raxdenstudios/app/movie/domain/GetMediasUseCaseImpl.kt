package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.PageList

internal class GetMediasUseCaseImpl(
  private val movieRepository: MovieRepository
) : GetMediasUseCase {

  override suspend fun execute(params: GetMediasUseCase.Params): ResultData<PageList<Media>> =
    movieRepository.movies(params.mediaFilter, params.page, params.pageSize)
}