package com.raxdenstudios.app.login.domain

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.movie.data.repository.MediaRepository
import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.commons.DispatcherFacade
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LoginUseCase(
  private val dispatcher: DispatcherFacade,
  private val accountRepository: AccountRepository,
  private val mediaRepository: MediaRepository,
) {

  suspend fun execute(credentials: Credentials) {
    accountRepository.createAccountWithCredentials(credentials)
    withContext(dispatcher.io()) {
      launch { mediaRepository.loadWatchListInLocal(MediaType.Movie) }
      launch { mediaRepository.loadWatchListInLocal(MediaType.TVShow) }
    }
  }
}
