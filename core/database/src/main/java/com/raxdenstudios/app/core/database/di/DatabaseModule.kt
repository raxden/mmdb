package com.raxdenstudios.app.core.database.di

import android.content.Context
import com.raxdenstudios.app.core.database.AccountDatabase
import com.raxdenstudios.app.core.database.HomeModuleDatabase
import com.raxdenstudios.app.core.database.MediaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AccountDatabaseModule {

    @Provides
    fun provideAccountDatabase(@ApplicationContext context: Context): AccountDatabase =
        AccountDatabase.getInstance(context)
}

@Module
@InstallIn(SingletonComponent::class)
object MediaDatabaseModule {

    @Provides
    fun provideMediaDatabase(@ApplicationContext context: Context): MediaDatabase =
        MediaDatabase.getInstance(context)
}

@Module
@InstallIn(SingletonComponent::class)
object HomeDataModule {

    @Provides
    fun provideHomeModuleDatabase(@ApplicationContext context: Context): HomeModuleDatabase =
        HomeModuleDatabase.getInstance(context)
}
