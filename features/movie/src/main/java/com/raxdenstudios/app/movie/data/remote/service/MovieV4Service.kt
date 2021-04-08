package com.raxdenstudios.app.movie.data.remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.network.model.ErrorDto
import com.raxdenstudios.app.network.model.PageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieV4Service {

  @GET("account/{account_id}/movie/watchlist")
  suspend fun watchList(
    @Path("account_id") accountId: String,
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MovieDto>, ErrorDto>
}
