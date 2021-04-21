package com.raxdenstudios.app.media.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.media.data.local.model.MediaEntity

@Dao
interface MediaDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(data: List<MediaEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(media: MediaEntity)

  @Query("SELECT * FROM movie WHERE watch_list = 1")
  suspend fun watchList(): List<MediaEntity>

  @Query("SELECT * FROM movie WHERE id == :movieId")
  suspend fun find(movieId: Long): MediaEntity?
}