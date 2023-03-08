package com.raxdenstudios.app.feature.di

import android.content.Context
import com.raxdenstudios.app.core.database.MediaDatabase
import com.raxdenstudios.app.core.database.di.MediaDatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MediaDatabaseModule::class]
)
object TestMediaDatabaseModule {

    @Provides
    fun provideMediaDatabase(@ApplicationContext context: Context): MediaDatabase =
        MediaDatabase.switchToInMemory(context)
}
