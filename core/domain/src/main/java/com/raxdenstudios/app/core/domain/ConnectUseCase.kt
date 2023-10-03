package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.AuthenticationRepository
import com.raxdenstudios.app.core.model.AccessToken
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.coFlatMap
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.core.model.Credentials
import javax.inject.Inject

class ConnectUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {

    suspend operator fun invoke(token: String): ResultData<Credentials, ErrorDomain> =
        authenticationRepository.requestAccessToken(token)
            .coFlatMap { accessToken -> initCredentials(accessToken) }

    private suspend fun initCredentials(accessToken: AccessToken): ResultData<Credentials, ErrorDomain> =
        authenticationRepository.requestSessionId(accessToken.value)
            .map { sessionId -> initCredentials(accessToken, sessionId) }

    private fun initCredentials(accessToken: AccessToken, sessionId: String): Credentials =
        Credentials(
            accountId = accessToken.accountId,
            accessToken = accessToken.value,
            sessionId = sessionId,
        )
}
