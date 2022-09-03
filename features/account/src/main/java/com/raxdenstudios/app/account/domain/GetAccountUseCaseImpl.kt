package com.raxdenstudios.app.account.domain

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.domain.model.Account
import javax.inject.Inject

internal class GetAccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository,
) : GetAccountUseCase {

    override suspend operator fun invoke(): Account =
        accountRepository.getAccount()
}
