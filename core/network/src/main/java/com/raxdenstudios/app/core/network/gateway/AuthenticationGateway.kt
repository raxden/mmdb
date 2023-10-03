package com.raxdenstudios.app.core.network.gateway

import com.raxdenstudios.app.core.network.model.AccessTokenRequestDto
import com.raxdenstudios.app.core.network.model.AccessTokenResponseDto
import com.raxdenstudios.app.core.network.model.NetworkErrorDto
import com.raxdenstudios.app.core.network.model.RequestTokenResponseDto
import com.raxdenstudios.app.core.network.model.SessionRequestDto
import com.raxdenstudios.app.core.network.model.SessionResponseDto
import com.raxdenstudios.app.core.network.service.AuthenticationV3Service
import com.raxdenstudios.app.core.network.service.AuthenticationV4Service
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.retrofit.toResultData
import javax.inject.Inject

class AuthenticationGateway @Inject constructor(
    private val authenticationV4Service: AuthenticationV4Service,
    private val authenticationV3Service: AuthenticationV3Service,
) {

    suspend fun requestToken(): ResultData<RequestTokenResponseDto, NetworkErrorDto> =
        authenticationV4Service.requestToken()
            .toResultData("Error occurred during requesting token")

    suspend fun requestAccessToken(token: String): ResultData<AccessTokenResponseDto, NetworkErrorDto> =
        authenticationV4Service.requestAccessToken(AccessTokenRequestDto(token))
            .toResultData("Error occurred during requesting token")

    suspend fun requestSessionId(accessToken: String): ResultData<SessionResponseDto, NetworkErrorDto> =
        authenticationV3Service.requestSessionId(SessionRequestDto(accessToken))
            .toResultData("Error occurred during requesting session")
}
