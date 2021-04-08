package com.raxdenstudios.app.account.domain.model

data class Credentials(
  val accountId: String,
  val accessToken: String,
  val sessionId: String,
)
