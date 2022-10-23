package com.raxdenstudios.app.account.domain

interface IsAccountLoggedUseCase {

    suspend operator fun invoke(): Boolean
}

