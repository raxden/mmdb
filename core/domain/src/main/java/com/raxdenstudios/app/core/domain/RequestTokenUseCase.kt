package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.AuthenticationRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.commons.ResultData
import javax.inject.Inject

class RequestTokenUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend operator fun invoke(): ResultData<String, ErrorDomain> =
        authenticationRepository.requestToken()
}
