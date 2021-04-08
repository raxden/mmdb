package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.commons.ResultData

interface AddMovieToWatchList {
  suspend fun execute(movieId: Long): ResultData<Boolean>
}
