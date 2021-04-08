package com.raxdenstudios.app.tmdb.data.remote.model

import com.google.gson.annotations.Expose

data class SessionResponseDto(
  @Expose val success: Boolean,
  @Expose val session_id: String,
)
