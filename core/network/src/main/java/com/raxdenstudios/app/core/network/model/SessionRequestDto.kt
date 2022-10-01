package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class SessionRequestDto(
    @Expose val access_token: String,
)
