package com.raxdenstudios.app.media.data.remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.data.remote.model.WatchListDto
import retrofit2.http.*

interface MediaV3Service {

  @GET("{media_type}/popular")
  suspend fun popular(
    @Path("media_type") mediaType: String,
    @Query("page") page: Int
  ): NetworkResponse<com.raxdenstudios.app.network.model.PageDto<MediaDto>, com.raxdenstudios.app.network.model.ErrorDto>

  @GET("{media_type}/now_playing")
  suspend fun nowPlaying(
    @Path("media_type") mediaType: String,
    @Query("page") page: Int
  ): NetworkResponse<com.raxdenstudios.app.network.model.PageDto<MediaDto>, com.raxdenstudios.app.network.model.ErrorDto>

  @GET("{media_type}/top_rated")
  suspend fun topRated(
    @Path("media_type") mediaType: String,
    @Query("page") page: Int
  ): NetworkResponse<com.raxdenstudios.app.network.model.PageDto<MediaDto>, com.raxdenstudios.app.network.model.ErrorDto>

  @GET("movie/upcoming")
  suspend fun upcoming(
    @Query("page") page: Int
  ): NetworkResponse<com.raxdenstudios.app.network.model.PageDto<MediaDto>, com.raxdenstudios.app.network.model.ErrorDto>

  @POST("account/{account_id}/watchlist")
  suspend fun watchList(
    @Path("account_id") accountId: String,
    @Body request: WatchListDto.Request
  ): NetworkResponse<WatchListDto.Response, com.raxdenstudios.app.network.model.ErrorDto>

  @GET("{media_type}/{movie_id}")
  suspend fun detail(
    @Path("media_type") mediaType: String,
    @Path("movie_id") movieId: String,
  ): NetworkResponse<MediaDto, com.raxdenstudios.app.network.model.ErrorDto>
}
