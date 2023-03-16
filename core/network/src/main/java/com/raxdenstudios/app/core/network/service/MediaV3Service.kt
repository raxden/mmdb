package com.raxdenstudios.app.core.network.service

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.core.network.model.ErrorDto
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.app.core.network.model.VideoDto
import com.raxdenstudios.app.core.network.model.WatchlistDto
import org.json.JSONObject
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

    @GET("movie/{media_id}")
    suspend fun movie(
        @Path("media_id") mediaId: String,
        @Query("append_to_response") appendToResponse: String = "release_dates",
    ): NetworkResponse<MediaDetailDto.Movie, ErrorDto>

    @GET("tv/{media_id}")
    suspend fun tvShow(
        @Path("media_id") mediaId: String,
        @Query("append_to_response") appendToResponse: String = "content_ratings",
    ): NetworkResponse<MediaDetailDto.TVShow, ErrorDto>

    @GET("movie/{media_id}/videos")
    suspend fun movieVideos(
        @Path("media_id") mediaId: String,
    ): NetworkResponse<PageDto<VideoDto>, ErrorDto>

    @GET("tv/{media_id}/videos")
    suspend fun tvVideos(
        @Path("media_id") mediaId: String,
    ): NetworkResponse<PageDto<VideoDto>, ErrorDto>

    @GET("movie/{media_id}/similar")
    suspend fun relatedMovies(
        @Path("media_id") mediaId: String,
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto>

    @GET("tv/{media_id}/similar")
    suspend fun relatedTVShows(
        @Path("media_id") mediaId: String,
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<MediaDto.TVShow>, ErrorDto>

    @GET("search/multi")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): NetworkResponse<PageDto<JSONObject>, ErrorDto>
}
