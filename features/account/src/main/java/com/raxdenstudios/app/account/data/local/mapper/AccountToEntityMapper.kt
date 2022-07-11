package com.raxdenstudios.app.account.data.local.mapper

import com.raxdenstudios.app.account.data.local.model.AccountEntity
import com.raxdenstudios.app.account.data.local.model.TMDBCredentialsEntity
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.commons.util.DataMapper
import javax.inject.Inject

internal class AccountEntityToDomainMapper @Inject constructor() :
  DataMapper<AccountEntity, Account>() {

  override fun transform(source: AccountEntity): Account =
    if (source.tmdbCredentials != null) {
      Account.Logged(
        id = source.id,
        credentials = Credentials(
          source.tmdbCredentials.accountId,
          source.tmdbCredentials.accessToken,
          source.tmdbCredentials.sessionId,
        )
      )
    } else {
      Account.Guest(
        id = source.id,
      )
    }
}

internal class AccountToEntityMapper @Inject constructor() : DataMapper<Account, AccountEntity>() {

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
