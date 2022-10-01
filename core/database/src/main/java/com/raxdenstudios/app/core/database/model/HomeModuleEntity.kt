package com.raxdenstudios.app.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaType

@Entity(tableName = "home_module")
data class HomeModuleEntity(
//  TODO PRIMARY KEY TYPE AND MEDIACATEGORY AND MEDIATYPE
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "mediaCategory") val mediaCategory: Int,
    @ColumnInfo(name = "mediaType") val mediaType: Int,
    @ColumnInfo(name = "order") val order: Int,
) {

    companion object {

        const val MODULE_MEDIAS = 1

        val popular = HomeModuleEntity(
            type = MODULE_MEDIAS,
            mediaCategory = MediaCategory.Popular.value,
            mediaType = MediaType.Movie.value,
            order = 2
        )
        val nowPlaying = HomeModuleEntity(
            type = MODULE_MEDIAS,
            mediaCategory = MediaCategory.NowPlaying.value,
            mediaType = MediaType.Movie.value,
            order = 1
        )
        val topRated = HomeModuleEntity(
            type = MODULE_MEDIAS,
            mediaCategory = MediaCategory.TopRated.value,
            mediaType = MediaType.Movie.value,
            order = 4
        )
        val upcoming = HomeModuleEntity(
            type = MODULE_MEDIAS,
            mediaCategory = MediaCategory.Upcoming.value,
            mediaType = MediaType.Movie.value,
            order = 5
        )
        val watchList = HomeModuleEntity(
            type = MODULE_MEDIAS,
            mediaCategory = MediaCategory.Watchlist.value,
            mediaType = MediaType.Movie.value,
            order = 3
        )
    }
}
