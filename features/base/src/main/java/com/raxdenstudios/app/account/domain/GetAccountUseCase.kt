package com.raxdenstudios.app.account.domain

import com.raxdenstudios.app.account.domain.model.Account

interface GetAccountUseCase {
  suspend fun execute(): Account
}