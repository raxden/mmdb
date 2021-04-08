package com.raxdenstudios.app.account.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.account.data.local.model.AccountEntity

@Dao
interface AccountDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(account: AccountEntity)

  @Query("SELECT * FROM account WHERE id == 1")
  suspend fun get(): AccountEntity?

  @Query("DELETE FROM account")
  suspend fun clear()
}
