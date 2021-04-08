package com.raxdenstudios.app.tmdb.domain

import com.raxdenstudios.app.tmdb.data.repository.AuthenticationRepository
import com.raxdenstudios.commons.ResultData

internal class RequestTokenUseCase(
  private val authenticationRepository: AuthenticationRepository
) {

  suspend fun execute(): ResultData<String> =
    authenticationRepository.requestToken()
}
