package com.raxdenstudios.app.account.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.account.data.local.model.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account WHERE id == 1")
    fun observe(): Flow<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Query("SELECT * FROM account WHERE id == 1")
    suspend fun get(): AccountEntity?

    @Query("DELETE FROM account")
    suspend fun clear()
}
