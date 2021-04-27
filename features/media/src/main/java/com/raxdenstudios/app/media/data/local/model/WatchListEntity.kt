package com.raxdenstudios.app.media.data.local.model

import androidx.annotation.VisibleForTesting
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
  tableName = "watch_list",
  foreignKeys = [
    ForeignKey(
      entity = MediaEntity::class,
      parentColumns = arrayOf("id"),
      childColumns = arrayOf("media_id"),
      onDelete = ForeignKey.CASCADE
    )
  ]
)
data class WatchListEntity(
  @PrimaryKey @ColumnInfo(name = "media_id") val mediaId: Long,
) {

  companion object {
    @VisibleForTesting
    val empty = WatchListEntity(
      mediaId = 0L,
    )
  }
}
