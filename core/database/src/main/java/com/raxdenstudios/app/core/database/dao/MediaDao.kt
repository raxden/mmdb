package com.raxdenstudios.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.core.database.model.MediaEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM media WHERE id == :mediaId")
    fun observe(mediaId: Long): Flow<MediaEntity?>

    @Query("SELECT * FROM media INNER JOIN watch_list ON media.id == watch_list.media_id WHERE type == :mediaType")
    fun observe(mediaType: Int): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media INNER JOIN watch_list ON media.id == watch_list.media_id")
    fun observe(): Flow<List<MediaEntity>>
}
