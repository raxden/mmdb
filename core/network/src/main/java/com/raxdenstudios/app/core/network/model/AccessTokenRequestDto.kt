package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class AccessTokenRequestDto(
    @Expose val request_token: String,
)
