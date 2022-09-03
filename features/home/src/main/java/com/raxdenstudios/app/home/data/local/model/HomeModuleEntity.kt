package com.raxdenstudios.app.home.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_module")
data class HomeModuleEntity(
//  TODO PRIMARY KEY TYPE AND SUBTYPE
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "subtype") val subtype: Int,
    @ColumnInfo(name = "order") val order: Int,
) {

    companion object {
        val popular = HomeModuleEntity(type = 1, subtype = 1, order = 2)
        val nowPlaying = HomeModuleEntity(type = 1, subtype = 2, order = 1)
        val topRated = HomeModuleEntity(type = 1, subtype = 3, order = 4)
        val upcoming = HomeModuleEntity(type = 1, subtype = 4, order = 5)
        val watchList = HomeModuleEntity(type = 1, subtype = 5, order = 3)
    }
}
