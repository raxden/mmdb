package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.AccountRepository
import com.raxdenstudios.core.domain.GetAccountUseCase
import com.raxdenstudios.core.model.Account
import javax.inject.Inject

class GetAccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository,
) : GetAccountUseCase {

    override suspend operator fun invoke(): Account =
        accountRepository.getAccount()
}
