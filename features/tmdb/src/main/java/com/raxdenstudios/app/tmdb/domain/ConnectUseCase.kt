package com.raxdenstudios.app.tmdb.domain

import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.tmdb.data.repository.AuthenticationRepository
import com.raxdenstudios.app.tmdb.domain.model.AccessToken
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.map
import javax.inject.Inject

internal class ConnectUseCase @Inject constructor(
  private val authenticationRepository: AuthenticationRepository
) {

  suspend operator fun invoke(token: String): ResultData<Credentials> =
    when (val accessTokenResult = authenticationRepository.requestAccessToken(token)) {
      is ResultData.Error -> ResultData.Error(accessTokenResult.throwable)
      is ResultData.Success -> initCredentials(accessTokenResult.value)
    }

  private suspend fun initCredentials(accessToken: AccessToken) =
    authenticationRepository.requestSessionId(accessToken.value)
      .map { sessionId -> initCredentials(accessToken, sessionId) }

  private fun initCredentials(accessToken: AccessToken, sessionId: String) =
    Credentials(
      accountId = accessToken.accountId,
      accessToken = accessToken.value,
      sessionId = sessionId,
    )
}

