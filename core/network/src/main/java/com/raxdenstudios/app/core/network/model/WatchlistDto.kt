package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
sealed class WatchlistDto {

    sealed class Request(
        @Expose val media_type: String,
        @Expose val media_id: Long,
        @Expose val watchlist: Boolean = true,
    ) : WatchlistDto() {

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
    ) : WatchlistDto() {

        companion object {

            val empty = Response(
                success = true,
                status_code = 200,
                status_message = ""
            )
        }
    }
}
