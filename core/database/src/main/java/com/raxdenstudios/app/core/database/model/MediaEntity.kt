package com.raxdenstudios.app.core.database.model

import androidx.annotation.VisibleForTesting
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
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "type") val type: Int,
    @Embedded(prefix = "backdrop_") val backdrop: PictureEntity?,
    @Embedded(prefix = "poster_") val poster: PictureEntity?,
    @ColumnInfo(name = "release") val release: Long,
    @Embedded(prefix = "vote_") val vote: VoteEntity,
) {

    companion object {

        @VisibleForTesting
        val mock = MediaEntity(
            id = 1L,
            title = "The Last of Us",
            overview = "Twenty years after modern civilization has been destroyed...",
            type = 1,
            backdrop = PictureEntity.mock,
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

        @VisibleForTesting
        val mock = PictureEntity(
            thumbnail = SizeEntity.empty.copy(
                type = "thumbnail",
                url = "https://image.tmdb.org/t/p/w500/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg"
            ),
            original = SizeEntity.empty.copy(
                type = "original",
                url = "https://image.tmdb.org/t/p/original/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg"
            ),
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
