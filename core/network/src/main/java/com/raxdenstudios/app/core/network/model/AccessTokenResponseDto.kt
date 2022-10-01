package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class AccessTokenResponseDto(
    @Expose val access_token: String,
    @Expose val account_id: String,
    @Expose val status_code: Int,
    @Expose val status_message: String,
    @Expose val success: Boolean,
)

