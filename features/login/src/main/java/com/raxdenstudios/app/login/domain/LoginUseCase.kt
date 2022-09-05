package com.raxdenstudios.app.login.domain

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.DispatcherProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LoginUseCase @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val accountRepository: AccountRepository,
    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(credentials: Credentials) {
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
