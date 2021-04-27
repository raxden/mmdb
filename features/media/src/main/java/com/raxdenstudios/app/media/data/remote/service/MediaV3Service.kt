package com.raxdenstudios.app.media.data.remote.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.data.remote.model.WatchListDto
import com.raxdenstudios.app.network.model.ErrorDto
import com.raxdenstudios.app.network.model.PageDto
import retrofit2.http.*

interface MediaV3Service {

  @GET("movie/popular")
  suspend fun popularMovies(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

  @GET("tv/popular")
  suspend fun popularTVShows(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MediaDto.TVShow>, ErrorDto>

  @GET("movie/now_playing")
  suspend fun nowPlayingMovies(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

  @GET("movie/top_rated")
  suspend fun topRatedMovies(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

  @GET("tv/top_rated")
  suspend fun topRatedTVShows(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MediaDto.TVShow>, ErrorDto>

  @GET("movie/upcoming")
  suspend fun upcoming(
    @Query("page") page: Int
  ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

  @POST("account/{account_id}/watchlist")
  suspend fun watchList(
    @Path("account_id") accountId: String,
    @Body request: WatchListDto.Request
  ): NetworkResponse<WatchListDto.Response, ErrorDto>

  @GET("movie/{movie_id}")
  suspend fun detailMovie(
    @Path("movie_id") movieId: String,
  ): NetworkResponse<MediaDto.Movie, ErrorDto>

  @GET("tv/{movie_id}")
  suspend fun detailTVShow(
    @Path("movie_id") movieId: String,
  ): NetworkResponse<MediaDto.TVShow, ErrorDto>
}
