package com.raxdenstudios.app.core.data.remote.datasource

import com.raxdenstudios.app.core.data.remote.mapper.NetworkErrorDtoToErrorMapper
import com.raxdenstudios.app.core.model.AccessToken
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.network.gateway.AuthenticationGateway
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.mapFailure
import javax.inject.Inject

class AuthenticationRemoteDataSource @Inject constructor(
    private val gateway: AuthenticationGateway,
    private val networkErrorDtoToErrorMapper: NetworkErrorDtoToErrorMapper,
) {

    suspend fun requestToken(): ResultData<String, ErrorDomain> =
        gateway.requestToken()
            .map { dto -> dto.request_token }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun requestAccessToken(token: String): ResultData<AccessToken, ErrorDomain> =
        gateway.requestAccessToken(token)
            .map { dto -> AccessToken(dto.access_token, dto.account_id) }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }

    suspend fun requestSessionId(accessToken: String): ResultData<String, ErrorDomain> =
        gateway.requestSessionId(accessToken)
            .map { dto -> dto.session_id }
            .mapFailure { error -> networkErrorDtoToErrorMapper.transform(error) }
}
