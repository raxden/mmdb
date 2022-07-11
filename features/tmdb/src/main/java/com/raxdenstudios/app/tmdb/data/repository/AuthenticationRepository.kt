package com.raxdenstudios.app.tmdb.data.repository

import com.raxdenstudios.app.tmdb.data.remote.datasource.AuthenticationRemoteDataSource
import com.raxdenstudios.app.tmdb.domain.model.AccessToken
import com.raxdenstudios.commons.ResultData
import javax.inject.Inject

internal class AuthenticationRepository @Inject constructor(
  private val authenticationRemoteDataSource: AuthenticationRemoteDataSource
) {

  suspend fun requestToken(): ResultData<String> =
    authenticationRemoteDataSource.requestToken()

  suspend fun requestAccessToken(token: String): ResultData<AccessToken> =
    authenticationRemoteDataSource.requestAccessToken(token)

  suspend fun requestSessionId(accessToken: String): ResultData<String> =
    authenticationRemoteDataSource.requestSessionId(accessToken)
}
