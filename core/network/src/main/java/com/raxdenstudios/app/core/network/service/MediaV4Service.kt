package com.raxdenstudios.app.core.network.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.ErrorDto
import com.raxdenstudios.app.core.network.model.PageDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaV4Service {

    @GET("account/{account_id}/movie/watchlist")
    @Headers("Cache-Control: no-cache")
    suspend fun watchListMovies(
        @Path("account_id") accountId: String,
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

    @GET("account/{account_id}/tv/watchlist")
    @Headers("Cache-Control: no-cache")
    suspend fun watchListTVShows(
        @Path("account_id") accountId: String,
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.TVShow>, ErrorDto>
}
