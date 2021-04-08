package com.raxdenstudios.app.network

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.domain.model.Account
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class SessionInterceptor(
  private val accountRepository: AccountRepository
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
    return when (val account = accountRepository.getAccount()) {
      is Account.Logged -> account.credentials.sessionId
      is Account.Guest -> ""
    }
  }
}
