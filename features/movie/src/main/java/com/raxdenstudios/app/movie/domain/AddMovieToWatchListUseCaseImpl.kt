package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MediaRepository
import com.raxdenstudios.commons.ResultData

internal class AddMovieToWatchListUseCaseImpl(
  private val mediaRepository: MediaRepository
) : AddMovieToWatchListUseCase {

  override suspend fun execute(params: AddMovieToWatchListUseCase.Params): ResultData<Boolean> =
    mediaRepository.addMediaToWatchList(params.movieId, params.mediaType)
}
