package com.raxdenstudios.app.home.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_module")
data class HomeModuleEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  @ColumnInfo(name = "type") val type: Int,
  @ColumnInfo(name = "subtype") val subtype: Int,
  @ColumnInfo(name = "order") val order: Int,
)
