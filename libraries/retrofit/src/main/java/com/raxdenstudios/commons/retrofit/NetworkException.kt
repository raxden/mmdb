package com.raxdenstudios.commons.retrofit

import java.io.IOException

sealed class NetworkException(
  message: String,
  cause: Throwable? = null
) : Throwable(message, cause) {

  data class Client(
    val code: Int,
    override val message: String,
  ) : NetworkException(message)

  data class Server(
    val code: Int,
    override val message: String,
  ) : NetworkException(message)

  data class Network(
    override val message: String,
    override val cause: IOException,
  ) : NetworkException(message, cause)

  data class Unknown(
    override val message: String,
    override val cause: Throwable? = null,
  ) : NetworkException(message, cause)
}
