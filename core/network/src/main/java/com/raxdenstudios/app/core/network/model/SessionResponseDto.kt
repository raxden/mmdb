package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class SessionResponseDto(
    @Expose val success: Boolean,
    @Expose val session_id: String,
)
