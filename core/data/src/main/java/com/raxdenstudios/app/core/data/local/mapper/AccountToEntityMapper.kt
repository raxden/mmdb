package com.raxdenstudios.app.core.data.local.mapper

import com.raxdenstudios.app.core.database.model.AccountEntity
import com.raxdenstudios.app.core.database.model.TMDBCredentialsEntity
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.core.model.Account
import com.raxdenstudios.core.model.Credentials
import javax.inject.Inject

class AccountEntityToDomainMapper @Inject constructor() : DataMapper<AccountEntity, Account>() {

    override fun transform(source: AccountEntity): Account =
        if (source.tmdbCredentials != null) {
            Account.Logged(
                id = source.id,
                credentials = Credentials(
                    accountId = source.tmdbCredentials?.accountId ?: "",
                    accessToken = source.tmdbCredentials?.accessToken ?: "",
                    sessionId = source.tmdbCredentials?.sessionId ?: "",
                )
            )
        } else {
            Account.Guest(
                id = source.id,
            )
        }
}

class AccountToEntityMapper @Inject constructor() : DataMapper<Account, AccountEntity>() {

    override fun transform(source: Account): AccountEntity = when (source) {
        is Account.Guest -> AccountEntity(
            id = source.id,
            tmdbCredentials = null,
        )
        is Account.Logged -> AccountEntity(
            id = source.id,
            tmdbCredentials = TMDBCredentialsEntity(
                accountId = source.credentials.accountId,
                accessToken = source.credentials.accessToken,
                sessionId = source.credentials.sessionId,
            )
        )
    }
}
