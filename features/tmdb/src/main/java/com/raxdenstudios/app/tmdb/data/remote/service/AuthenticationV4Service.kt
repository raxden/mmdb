package com.raxdenstudios.app.tmdb.data.remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.network.model.ErrorDto
import com.raxdenstudios.app.tmdb.data.remote.model.AccessTokenRequestDto
import com.raxdenstudios.app.tmdb.data.remote.model.AccessTokenResponseDto
import com.raxdenstudios.app.tmdb.data.remote.model.RequestTokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationV4Service {

  @POST("auth/request_token")
  suspend fun requestToken(): NetworkResponse<RequestTokenResponseDto, ErrorDto>

  @POST("auth/access_token")
  suspend fun requestAccessToken(
    @Body token: AccessTokenRequestDto
  ): NetworkResponse<AccessTokenResponseDto, ErrorDto>
}
