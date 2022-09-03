package com.raxdenstudios.app.tmdb.domain

import com.raxdenstudios.app.tmdb.data.repository.AuthenticationRepository
import com.raxdenstudios.commons.ResultData
import javax.inject.Inject

internal class RequestTokenUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend operator fun invoke(): ResultData<String> =
        authenticationRepository.requestToken()
}
