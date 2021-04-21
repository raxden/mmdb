package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.commons.ResultData

interface AddMediaToWatchListUseCase {
  suspend fun execute(params: Params): ResultData<Boolean>

  data class Params(
    val movieId: Long,
    val mediaType: MediaType
  )
}
