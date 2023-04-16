package com.raxdenstudios.app.core.database.di

import android.content.Context
import com.raxdenstudios.app.core.database.RecentSearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecentSearchDatabaseModule {

    @Provides
    fun provideRecentSearchDatabase(@ApplicationContext context: Context): RecentSearchDatabase =
        RecentSearchDatabase.getInstance(context)
}
