package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData

interface RemoveMediaFromWatchListUseCase {
  suspend fun execute(params: Params): ResultData<Boolean>

  data class Params(
    val movieId: Long,
    val mediaType: MediaType
  )
}
