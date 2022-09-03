package com.raxdenstudios.app.media.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.media.data.local.model.WatchListEntity

@Dao
interface WatchListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<WatchListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchListEntity)

    @Query("DELETE FROM watch_list WHERE media_id == :mediaId")
    suspend fun delete(mediaId: Long)

    @Query("SELECT * FROM watch_list WHERE media_id == :mediaId")
    suspend fun find(mediaId: Long): WatchListEntity?

    @Query("DELETE FROM watch_list")
    suspend fun clear()
}
