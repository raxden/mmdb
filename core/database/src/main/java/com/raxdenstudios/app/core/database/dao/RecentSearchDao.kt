package com.raxdenstudios.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.core.database.model.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RecentSearchEntity)

    @Query("SELECT * FROM recent_search")
    fun observe(): Flow<List<RecentSearchEntity>>
}
