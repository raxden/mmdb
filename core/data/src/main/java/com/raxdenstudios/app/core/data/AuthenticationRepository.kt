package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.data.remote.datasource.AuthenticationRemoteDataSource
import com.raxdenstudios.app.core.model.AccessToken
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.commons.ResultData
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) {

    suspend fun requestToken(): ResultData<String, ErrorDomain> =
        authenticationRemoteDataSource.requestToken()

    suspend fun requestAccessToken(token: String): ResultData<AccessToken, ErrorDomain> =
        authenticationRemoteDataSource.requestAccessToken(token)

    suspend fun requestSessionId(accessToken: String): ResultData<String, ErrorDomain> =
        authenticationRemoteDataSource.requestSessionId(accessToken)
}
