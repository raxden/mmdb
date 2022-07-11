package com.raxdenstudios.app.account.domain

interface IsAccountLoggedUseCase {
  suspend fun execute(): Boolean
}

