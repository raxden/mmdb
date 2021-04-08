package com.raxdenstudios.app.home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeModuleDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(data: List<HomeModuleEntity>)

  @Query("SELECT * FROM home_module ORDER BY `order`")
  suspend fun getAll(): List<HomeModuleEntity>

  @Query("SELECT * FROM home_module ORDER BY `order`")
  fun observeAll(): Flow<List<HomeModuleEntity>>
}
