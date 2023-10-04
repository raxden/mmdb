package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.AccountRepository
import com.raxdenstudios.commons.coroutines.DispatcherProvider
import com.raxdenstudios.core.model.Credentials
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val accountRepository: AccountRepository,
//    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(credentials: Credentials) {
        accountRepository.createAccountWithCredentials(credentials)
        loadWatchlistFromRemoteAndPersistInLocal()
    }

    private suspend fun loadWatchlistFromRemoteAndPersistInLocal() {
        withContext(dispatcher.io) {
//            launch { mediaRepository.watchList(MediaType.MOVIE) }
//            launch { mediaRepository.watchList(MediaType.TV_SHOW) }
        }
    }
}
