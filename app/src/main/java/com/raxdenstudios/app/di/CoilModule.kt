package com.raxdenstudios.app.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CoilModule {

    @Provides
    fun provideDiskCache(
        @ApplicationContext context: Context
    ): DiskCache = DiskCache.Builder()
        .directory(context.cacheDir.resolve(CACHE))
        .maxSizePercent(MAX_SIZE_PERCENT)
        .build()

    @Provides
    fun provideMemoryCache(
        @ApplicationContext context: Context
    ): MemoryCache =
        MemoryCache.Builder(context)
            .maxSizePercent(MAX_SIZE_PERCENT_MEMORY)
            .build()

    @Provides
    fun provideImageLoader(
        @ApplicationContext context: Context,
        diskCache: DiskCache,
        memoryCache: MemoryCache
    ) = ImageLoader.Builder(context)
        .diskCache { diskCache }
        .memoryCache { memoryCache }
        .build()

    companion object {

        private const val CACHE = "image_cache"
        private const val MAX_SIZE_PERCENT = 0.02
        private const val MAX_SIZE_PERCENT_MEMORY = 0.25
    }
}

