package com.raxdenstudios.app.media.data.remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaV4Service {

  @GET("account/{account_id}/{media_type}/watchlist")
  @Headers("Cache-Control: no-cache")
  suspend fun watchList(
    @Path("account_id") accountId: String,
    @Path("media_type") mediaType: String,
    @Query("page") page: Int
  ): NetworkResponse<com.raxdenstudios.app.network.model.PageDto<MediaDto>, com.raxdenstudios.app.network.model.ErrorDto>
}
