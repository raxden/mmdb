package com.raxdenstudios.commons.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

inline fun <reified T> RetrofitException.parseBodyError(): T? {
  val errorBody = errorBodyOrNull() ?: return null
  val retrofit = retrofitOrNull() ?: return null
  val converter = retrofit.responseBodyConverter<T>(T::class.java, arrayOfNulls(0))
  return converter.convert(errorBody)
}

fun RetrofitException.errorBodyOrNull(): ResponseBody? = when (this) {
  is RetrofitException.Non200Http -> response.errorBody()
  is RetrofitException.Network -> null
  is RetrofitException.Unexpected -> null
}

fun RetrofitException.retrofitOrNull(): Retrofit? = when (this) {
  is RetrofitException.Non200Http -> retrofit
  is RetrofitException.Network -> null
  is RetrofitException.Unexpected -> null
}

sealed class RetrofitException(
  error: Throwable?,
) : RuntimeException(error) {

  /**
   * A non-200 HTTP status code was received from the server.
   */
  sealed class Non200Http : RetrofitException(null) {
    abstract val url: String
    abstract val response: Response<*>
    abstract val retrofit: Retrofit

    data class Unauthenticated(
      override val url: String,
      override val response: Response<*>,
      override val retrofit: Retrofit,
    ) : Non200Http()

    data class Client(
      override val url: String,
      override val response: Response<*>,
      override val retrofit: Retrofit,
    ) : Non200Http()

    data class Server(
      override val url: String,
      override val response: Response<*>,
      override val retrofit: Retrofit,
    ) : Non200Http()
  }

  /**
   * An [IOException] occurred while communicating to the server.
   */
  data class Network(
    val error: IOException
  ) : RetrofitException(error)

  /**
   * An internal error occurred while attempting to execute a request. It is best practice to
   * re-throw this exception so your application crashes.
   */
  data class Unexpected(
    val error: Throwable
  ) : RetrofitException(error)
}
