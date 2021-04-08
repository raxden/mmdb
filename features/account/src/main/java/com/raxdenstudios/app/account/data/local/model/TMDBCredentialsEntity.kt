package com.raxdenstudios.app.account.data.local.model

data class TMDBCredentialsEntity(
  val accountId: String,
  val accessToken: String,
  val sessionId: String,
) {

  companion object {
    val default = TMDBCredentialsEntity(
      accountId = "",
      accessToken = "",
      sessionId = "",
    )
  }
}
