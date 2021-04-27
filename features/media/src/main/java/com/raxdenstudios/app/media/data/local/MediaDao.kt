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

  @Query("DELETE FROM media WHERE id == :mediaId")
  suspend fun delete(mediaId: Long)

  @Query("SELECT * FROM media WHERE id == :mediaId")
  suspend fun find(mediaId: Long): MediaEntity?

  @Query("SELECT * FROM media INNER JOIN watch_list ON media.id == watch_list.media_id WHERE type == :mediaType")
  suspend fun watchList(mediaType: Int): List<MediaEntity>
}