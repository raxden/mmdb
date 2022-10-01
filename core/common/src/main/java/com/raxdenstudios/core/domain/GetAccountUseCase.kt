package com.raxdenstudios.core.domain

import com.raxdenstudios.core.model.Account

interface GetAccountUseCase {

    suspend operator fun invoke(): Account
}
