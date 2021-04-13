package com.raxdenstudios.app.account.domain

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.domain.model.Account

internal class IsAccountLoggedUseCaseImpl(
  private val accountRepository: AccountRepository
) : IsAccountLoggedUseCase {

  override suspend fun execute(): Boolean =
    accountRepository.getAccount() is Account.Logged
}
