package com.raxdenstudios.app.core.data.remote.datasource

import com.raxdenstudios.app.core.network.gateway.AuthenticationGateway
import com.raxdenstudios.app.core.model.AccessToken
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.map
import javax.inject.Inject

class AuthenticationRemoteDataSource @Inject constructor(
    private val gateway: AuthenticationGateway,
) {

    suspend fun requestToken(): ResultData<String> =
        gateway.requestToken().map { dto -> dto.request_token }

    suspend fun requestAccessToken(token: String): ResultData<AccessToken> =
        gateway.requestAccessToken(token).map { dto -> AccessToken(dto.access_token, dto.account_id) }

    suspend fun requestSessionId(accessToken: String): ResultData<String> =
        gateway.requestSessionId(accessToken).map { dto -> dto.session_id }
}
