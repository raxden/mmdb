package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.AccountRepository
import com.raxdenstudios.core.model.Account

class IsAccountLoggedUseCase(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(): Boolean =
        accountRepository.getAccount() is Account.Logged
}
