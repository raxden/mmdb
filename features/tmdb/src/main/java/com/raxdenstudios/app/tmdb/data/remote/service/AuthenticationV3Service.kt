package com.raxdenstudios.app.tmdb.data.remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.tmdb.data.remote.model.SessionRequestDto
import com.raxdenstudios.app.tmdb.data.remote.model.SessionResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationV3Service {

  @POST("authentication/session/convert/4")
  suspend fun requestSessionId(
    @Body dto: SessionRequestDto
  ): NetworkResponse<SessionResponseDto, com.raxdenstudios.app.network.model.ErrorDto>
}
