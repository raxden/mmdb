package com.raxdenstudios.app.tmdb.data.remote.model

import com.google.gson.annotations.Expose

data class AccessTokenRequestDto(
    @Expose val request_token: String,
)
