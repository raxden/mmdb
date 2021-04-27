package com.raxdenstudios.app.login.domain

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.domain.model.MediaType
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
    loadWatchListFromRemoteAndPersistInLocal()
  }

  private suspend fun loadWatchListFromRemoteAndPersistInLocal() {
    withContext(dispatcher.io()) {
      launch { mediaRepository.watchList(MediaType.MOVIE) }
      launch { mediaRepository.watchList(MediaType.TV_SHOW) }
    }
  }
}
