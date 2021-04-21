package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData

interface AddMediaToWatchListUseCase {
  suspend fun execute(params: Params): ResultData<Boolean>

  data class Params(
    val mediaId: Long,
    val mediaType: MediaType
  )
}
