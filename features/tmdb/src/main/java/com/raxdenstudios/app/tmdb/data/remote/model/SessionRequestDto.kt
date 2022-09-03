package com.raxdenstudios.app.tmdb.data.remote.model

import com.google.gson.annotations.Expose

data class SessionRequestDto(
    @Expose val access_token: String,
)
