package com.raxdenstudios.app.media.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_list")
data class WatchListEntity(
  @PrimaryKey @ColumnInfo(name = "media_id") val mediaId: Long,
) {

  companion object {
    val empty = WatchListEntity(
      mediaId = 0L,
    )
  }
}
