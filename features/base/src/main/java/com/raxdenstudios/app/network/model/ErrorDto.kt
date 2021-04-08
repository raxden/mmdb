package com.raxdenstudios.app.network.model

import com.google.gson.annotations.Expose

data class ErrorDto(
  @Expose val status_message: String,
  @Expose val status_code: Int,
)
