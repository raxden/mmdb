package com.raxdenstudios.app.media.di

import android.content.Context
import com.raxdenstudios.app.media.data.local.MediaDao
import com.raxdenstudios.app.media.data.local.MediaDatabase
import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.remote.service.MediaV3Service
import com.raxdenstudios.app.media.data.remote.service.MediaV4Service
import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.data.repository.MediaRepositoryImpl
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCaseImpl
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCaseImpl
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCaseImpl
import com.raxdenstudios.app.network.di.APIVersionV3
import com.raxdenstudios.app.network.di.APIVersionV4
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MediaDataModule {

  @Provides
  fun provideMediaDatabase(@ApplicationContext context: Context): MediaDatabase =
    MediaDatabase.getInstance(context)

  @Provides
  fun provideMediaDAO(mediaDatabase: MediaDatabase): MediaDao =
    mediaDatabase.mediaDao()

  @Provides
  fun provideWatchListDAO(mediaDatabase: MediaDatabase): WatchListDao =
    mediaDatabase.watchListDao()

  @Provides
  fun provideMediaV3Service(@APIVersionV3 retrofit: Retrofit): MediaV3Service =
    retrofit.create(MediaV3Service::class.java)

  @Provides
  fun provideMediaV4Service(@APIVersionV4 retrofit: Retrofit): MediaV4Service =
    retrofit.create(MediaV4Service::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaDataBindsModule {

  @Binds
  internal abstract fun bindMediaRepository(useCase: MediaRepositoryImpl): MediaRepository

  @Binds
  internal abstract fun bindAddMediaToWatchListUseCase(
    useCase: AddMediaToWatchListUseCaseImpl
  ): AddMediaToWatchListUseCase

  @Binds
  internal abstract fun bindRemoveMediaFromWatchListUseCase(
    useCase: RemoveMediaFromWatchListUseCaseImpl
  ): RemoveMediaFromWatchListUseCase

  @Binds
  internal abstract fun bindGetMediasUseCase(useCase: GetMediasUseCaseImpl): GetMediasUseCase
}
