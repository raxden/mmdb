package com.raxdenstudios.app.account.domain

interface IsAccountLogged {
  suspend fun execute(): Boolean
}
