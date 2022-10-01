package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class RequestTokenResponseDto(
    @Expose val request_token: String,
    @Expose val status_code: Int,
    @Expose val status_message: String,
    @Expose val success: Boolean,
)

