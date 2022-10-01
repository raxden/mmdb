package com.raxdenstudios.app.core.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.core.database.dao.AccountDao
import com.raxdenstudios.app.core.database.model.AccountEntity

@Database(
    entities = [AccountEntity::class],
    version = 1,
)
abstract class AccountDatabase : RoomDatabase() {

    abstract fun dao(): AccountDao

    companion object {

        @Volatile
        private var INSTANCE: AccountDatabase? = null

        fun getInstance(context: Context): AccountDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        @VisibleForTesting
        fun switchToInMemory(context: Context): AccountDatabase {
            return Room.inMemoryDatabaseBuilder(context, AccountDatabase::class.java)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build()
        }

        private fun buildDatabase(context: Context): AccountDatabase {
            return Room.databaseBuilder(context, AccountDatabase::class.java, "account.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
