package com.raxdenstudios.app.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search", indices = [Index(value = ["query"], unique = true)])
data class RecentSearchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "query") val query: String,
)
