package com.raxdenstudios.app.movie.data.remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.data.remote.model.WatchListDto
import com.raxdenstudios.app.network.model.ErrorDto
import com.raxdenstudios.app.network.model.PageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieV3Service {

  @GET("movie/popular")
  suspend fun popular(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MovieDto>, ErrorDto>

  @GET("movie/now_playing")
  suspend fun nowPlaying(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MovieDto>, ErrorDto>

  @GET("movie/top_rated")
  suspend fun topRated(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MovieDto>, ErrorDto>

  @GET("movie/upcoming")
  suspend fun upcoming(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MovieDto>, ErrorDto>

  @POST("account/{account_id}/watchlist")
  suspend fun watchList(
    @Path("account_id") accountId: String,
    @Body request: WatchListDto.Request
  ): NetworkResponse<WatchListDto.Response, ErrorDto>

  @GET("movie/{movie_id}")
  suspend fun detail(
    @Path("movie_id") movieId: String,
  ): NetworkResponse<MovieDto, ErrorDto>
}
