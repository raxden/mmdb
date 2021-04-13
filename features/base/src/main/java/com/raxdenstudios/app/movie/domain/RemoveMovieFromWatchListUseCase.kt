package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.commons.ResultData

interface RemoveMovieFromWatchListUseCase {
  suspend fun execute(movieId: Long): ResultData<Boolean>
}
