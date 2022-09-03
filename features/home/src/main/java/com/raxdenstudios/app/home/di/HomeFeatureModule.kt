package com.raxdenstudios.app.home.di

import android.content.Context
import com.raxdenstudios.app.home.data.local.HomeModuleDao
import com.raxdenstudios.app.home.data.local.HomeModuleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HomeFeatureModule {

    @Provides
    fun provideHomeModuleDatabase(@ApplicationContext context: Context): HomeModuleDatabase =
        HomeModuleDatabase.getInstance(context)

    @Provides
    fun provideAccountDAO(homeModuleDatabase: HomeModuleDatabase): HomeModuleDao =
        homeModuleDatabase.dao()
}
