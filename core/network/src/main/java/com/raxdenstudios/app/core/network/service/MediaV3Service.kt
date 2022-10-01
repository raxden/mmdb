package com.raxdenstudios.app.core.network.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.WatchlistDto
import com.raxdenstudios.app.core.network.model.ErrorDto
import com.raxdenstudios.app.core.network.model.PageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaV3Service {

    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

    @GET("tv/popular")
    suspend fun popularTVShows(
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.TVShow>, ErrorDto>

    @GET("movie/now_playing")
    suspend fun nowPlayingMovies(
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

    @GET("movie/top_rated")
    suspend fun topRatedMovies(
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

    @GET("tv/top_rated")
    suspend fun topRatedTVShows(
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.TVShow>, ErrorDto>

    @GET("movie/upcoming")
    suspend fun upcoming(
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

    @POST("account/{account_id}/watchlist")
    suspend fun watchList(
        @Path("account_id") accountId: String,
        @Body request: WatchlistDto.Request,
    ): NetworkResponse<WatchlistDto.Response, ErrorDto>

    @GET("movie/{movie_id}")
    suspend fun movie(
        @Path("movie_id") movieId: String,
    ): NetworkResponse<MediaDto.Movie, ErrorDto>

    @GET("tv/{movie_id}")
    suspend fun tvShow(
        @Path("movie_id") movieId: String,
    ): NetworkResponse<MediaDto.TVShow, ErrorDto>
}
