package com.raxdenstudios.app.tmdb.data.remote.model

import com.google.gson.annotations.Expose

data class RequestTokenResponseDto(
    @Expose val request_token: String,
    @Expose val status_code: Int,
    @Expose val status_message: String,
    @Expose val success: Boolean,
)

