package com.raxdenstudios.app.account.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey val id: Long = 0,
    @Embedded(prefix = "tmdb_credentials_")
    val tmdbCredentials: TMDBCredentialsEntity?,
) {

    companion object {

        val guestAccount = AccountEntity(
            id = 1,
            tmdbCredentials = TMDBCredentialsEntity.default
        )
    }
}

