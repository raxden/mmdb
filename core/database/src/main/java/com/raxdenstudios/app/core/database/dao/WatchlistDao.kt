package com.raxdenstudios.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.core.database.model.WatchlistEntity

@Dao
interface WatchlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<WatchlistEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchlistEntity)

    @Query("DELETE FROM watch_list WHERE media_id == :mediaId")
    suspend fun delete(mediaId: Long)

    @Query("SELECT * FROM watch_list WHERE media_id == :mediaId")
    suspend fun find(mediaId: Long): WatchlistEntity?

    @Query("DELETE FROM watch_list")
    suspend fun clear()
}
