package com.raxdenstudios.app.core.network.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.core.network.model.AccessTokenRequestDto
import com.raxdenstudios.app.core.network.model.AccessTokenResponseDto
import com.raxdenstudios.app.core.network.model.RequestTokenResponseDto
import com.raxdenstudios.app.core.network.model.ErrorDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationV4Service {

    @POST("auth/request_token")
    suspend fun requestToken(): NetworkResponse<RequestTokenResponseDto, ErrorDto>

    @POST("auth/access_token")
    suspend fun requestAccessToken(
        @Body token: AccessTokenRequestDto,
    ): NetworkResponse<AccessTokenResponseDto, ErrorDto>
}
