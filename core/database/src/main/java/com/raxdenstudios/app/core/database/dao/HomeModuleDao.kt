package com.raxdenstudios.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.raxdenstudios.app.core.database.model.HomeModuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeModuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<HomeModuleEntity>)

    @Update
    suspend fun update(data: HomeModuleEntity)

    @Query("SELECT * FROM home_module ORDER BY `order`")
    suspend fun getAll(): List<HomeModuleEntity>

    @Query("SELECT * FROM home_module ORDER BY `order`")
    fun observeAll(): Flow<List<HomeModuleEntity>>

    @Query("SELECT * FROM home_module WHERE id = :id")
    suspend fun get(id: Long): HomeModuleEntity
}
