package com.raxdenstudios.app.core.network.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.core.network.model.SessionRequestDto
import com.raxdenstudios.app.core.network.model.SessionResponseDto
import com.raxdenstudios.app.core.network.model.ErrorDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationV3Service {

    @POST("authentication/session/convert/4")
    suspend fun requestSessionId(
        @Body dto: SessionRequestDto,
    ): NetworkResponse<SessionResponseDto, ErrorDto>
}
