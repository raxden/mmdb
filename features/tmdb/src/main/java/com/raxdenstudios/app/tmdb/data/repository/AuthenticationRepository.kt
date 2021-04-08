package com.raxdenstudios.app.tmdb.data.repository

import com.raxdenstudios.app.tmdb.data.remote.datasource.AuthenticationRemoteDataSource
import com.raxdenstudios.app.tmdb.domain.model.AccessToken
import com.raxdenstudios.commons.ResultData

internal class AuthenticationRepository(
  private val authenticationRemoteDataSource: AuthenticationRemoteDataSource
) {

  suspend fun requestToken(): ResultData<String> =
    authenticationRemoteDataSource.requestToken()

  suspend fun requestAccessToken(token: String): ResultData<AccessToken> =
    authenticationRemoteDataSource.requestAccessToken(token)

  suspend fun requestSessionId(accessToken: String): ResultData<String> =
    authenticationRemoteDataSource.requestSessionId(accessToken)
}
