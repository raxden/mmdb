package com.raxdenstudios.app.tmdb.data.remote

import com.raxdenstudios.app.tmdb.data.remote.model.AccessTokenRequestDto
import com.raxdenstudios.app.tmdb.data.remote.model.AccessTokenResponseDto
import com.raxdenstudios.app.tmdb.data.remote.model.RequestTokenResponseDto
import com.raxdenstudios.app.tmdb.data.remote.model.SessionRequestDto
import com.raxdenstudios.app.tmdb.data.remote.model.SessionResponseDto
import com.raxdenstudios.app.tmdb.data.remote.service.AuthenticationV3Service
import com.raxdenstudios.app.tmdb.data.remote.service.AuthenticationV4Service
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.retrofit.toResultData
import javax.inject.Inject

internal class AuthenticationGateway @Inject constructor(
    private val authenticationV4Service: AuthenticationV4Service,
    private val authenticationV3Service: AuthenticationV3Service,
) {

    suspend fun requestToken(): ResultData<RequestTokenResponseDto> =
        authenticationV4Service.requestToken().toResultData("Error occurred during requesting token")

    suspend fun requestAccessToken(token: String): ResultData<AccessTokenResponseDto> =
        authenticationV4Service.requestAccessToken(
            AccessTokenRequestDto(token)
        ).toResultData("Error occurred during requesting token")

    suspend fun requestSessionId(accessToken: String): ResultData<SessionResponseDto> =
        authenticationV3Service.requestSessionId(
            SessionRequestDto(accessToken)
        ).toResultData("Error occurred during requesting session")
}
