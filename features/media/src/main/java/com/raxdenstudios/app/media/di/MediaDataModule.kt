package com.raxdenstudios.app.media.di

import com.raxdenstudios.app.media.data.local.MediaDatabase
import com.raxdenstudios.app.media.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.media.data.remote.MediaGateway
import com.raxdenstudios.app.media.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.media.data.remote.mapper.*
import com.raxdenstudios.app.media.data.remote.service.MediaV3Service
import com.raxdenstudios.app.media.data.remote.service.MediaV4Service
import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.data.repository.MediaRepositoryImpl
import com.raxdenstudios.app.media.domain.*
import com.raxdenstudios.app.network.model.APIVersion
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val mediaDataModule = module {

  single { MediaDatabase.getInstance(get()) }
  factory { get<MediaDatabase>().watchListDao() }

  factory { MediaLocalDataSource(get()) }

  single { get<Retrofit>(named(APIVersion.V3)).create(MediaV3Service::class.java) }
  single { get<Retrofit>(named(APIVersion.V4)).create(MediaV4Service::class.java) }

  factory { MediaGateway(get(), get(), get()) }

  factory { MediaTypeToDtoMapper() }
  factory { VoteDtoToDomainMapper() }
  factory { PictureDtoToDomainMapper(get(named(APIVersion.V3))) }
  factory { DateDtoToLocalDateMapper() }
  factory { MediaDtoToDomainMapper(get(), get(), get()) }

  factory { MediaRemoteDataSource(get(), get(), get()) }

  factory<MediaRepository> { MediaRepositoryImpl(get(), get(), get()) }

  factory<AddMediaToWatchListUseCase> { AddMediaToWatchListUseCaseImpl(get()) }
  factory<RemoveMediaFromWatchListUseCase> { RemoveMediaFromWatchListUseCaseImpl(get()) }
  factory<GetMediasUseCase> { GetMediasUseCaseImpl(get()) }
}
