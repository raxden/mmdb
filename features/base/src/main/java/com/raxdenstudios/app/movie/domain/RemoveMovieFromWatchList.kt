package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.commons.ResultData

interface RemoveMovieFromWatchList {
  suspend fun execute(movieId: Long): ResultData<Boolean>
}
