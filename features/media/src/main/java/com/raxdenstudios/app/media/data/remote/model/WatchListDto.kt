package com.raxdenstudios.app.media.data.remote.model

import com.google.gson.annotations.Expose

sealed class WatchListDto {

  sealed class Request(
    @Expose val media_type: String,
    @Expose val media_id: Long,
    @Expose val watchlist: Boolean = true,
  ) : WatchListDto() {

    data class Add(val movieId: Long, val category: String) : Request(
      media_type = category,
      media_id = movieId,
      watchlist = true,
    )

    data class Remove(val movieId: Long, val category: String) : Request(
      media_type = category,
      media_id = movieId,
      watchlist = false,
    )
  }

  data class Response(
    @Expose val success: Boolean,
    @Expose val status_code: Int,
    @Expose val status_message: String,
  ) : WatchListDto()
}



