package com.raxdenstudios.app.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
  @PrimaryKey @ColumnInfo(name = "id") val id: Long,
  @ColumnInfo(name = "title") val title: String,
  @Embedded(prefix = "backdrop_") val backdrop: PictureEntity?,
  @Embedded(prefix = "poster_") val poster: PictureEntity,
  @ColumnInfo(name = "release") val release: Long,
  @Embedded(prefix = "vote_") val vote: VoteEntity,
  @ColumnInfo(name = "watch_list") val watchList: Boolean,
) {

  companion object {
    val empty = MovieEntity(
      id = 0L,
      title = "",
      backdrop = PictureEntity.empty,
      poster = PictureEntity.empty,
      release = 0L,
      vote = VoteEntity.empty,
      watchList = false,
    )
  }
}

data class VoteEntity(
  @ColumnInfo(name = "average") val average: Float,
  @ColumnInfo(name = "count") val count: Int,
) {
  companion object {
    val empty = VoteEntity(
      average = 0.0f,
      count = 0,
    )
  }
}

data class PictureEntity(
  @Embedded(prefix = "thumbnail_") val thumbnail: SizeEntity,
  @Embedded(prefix = "original_") val original: SizeEntity,
) {
  companion object {
    val empty = PictureEntity(
      thumbnail = SizeEntity.empty.copy(type = "thumbnail"),
      original = SizeEntity.empty.copy(type = "original"),
    )
  }
}

data class SizeEntity(
  @ColumnInfo(name = "url") val url: String,
  @ColumnInfo(name = "type") val type: String,
) {
  companion object {
    val empty = SizeEntity(
      url = "",
      type = "",
    )
  }
}