package com.raxdenstudios.app.media.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raxdenstudios.commons.threeten.ext.toMilliseconds
import org.threeten.bp.LocalDate

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: Int,
    @Embedded(prefix = "backdrop_") val backdrop: PictureEntity?,
    @Embedded(prefix = "poster_") val poster: PictureEntity?,
    @ColumnInfo(name = "release") val release: Long,
    @Embedded(prefix = "vote_") val vote: VoteEntity,
) {

    companion object {
        val empty = MediaEntity(
            id = 0L,
            title = "",
            type = 0,
            backdrop = PictureEntity.empty,
            poster = PictureEntity.empty,
            release = LocalDate.of(1970, 1, 1).toMilliseconds(),
            vote = VoteEntity.empty
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
