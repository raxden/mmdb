package com.raxdenstudios.app.network

import com.raxdenstudios.app.account.domain.GetAccountUseCase
import com.raxdenstudios.app.account.domain.model.Account
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class SessionInterceptor @Inject constructor(
  private val getAccountUseCase: GetAccountUseCase,
) : Interceptor {

  companion object {
    private const val SESSION_ID = "session_id"
  }

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()
    runBlocking {
      val sessionId = getSessionId()
      val url = request.url.newBuilder()
        .addQueryParameter(SESSION_ID, sessionId)
        .build()
      request = request.newBuilder().url(url).build()
    }
    return chain.proceed(request)
  }

  private suspend fun getSessionId(): String {
    return when (val account = getAccountUseCase()) {
      is Account.Logged -> account.credentials.sessionId
      is Account.Guest -> ""
    }
  }
}
