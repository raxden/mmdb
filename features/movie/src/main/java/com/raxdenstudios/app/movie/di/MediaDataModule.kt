package com.raxdenstudios.app.movie.di

import com.raxdenstudios.app.movie.data.local.MediaDatabase
import com.raxdenstudios.app.movie.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.movie.data.local.mapper.*
import com.raxdenstudios.app.movie.data.remote.MediaGateway
import com.raxdenstudios.app.movie.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.movie.data.remote.mapper.*
import com.raxdenstudios.app.movie.data.remote.service.MediaV3Service
import com.raxdenstudios.app.movie.data.remote.service.MediaV4Service
import com.raxdenstudios.app.movie.data.repository.MediaRepository
import com.raxdenstudios.app.movie.data.repository.MediaRepositoryImpl
import com.raxdenstudios.app.movie.domain.*
import com.raxdenstudios.app.network.model.APIVersion
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val mediaDataModule = module {

  single { MediaDatabase.getInstance(get()) }
  factory { get<MediaDatabase>().watchListDao() }

  factory { MediaToEntityMapper(get(), get()) }
  factory { MediaEntityToDomainMapper(get(), get()) }
  factory { VoteToEntityMapper() }
  factory { VoteEntityToDomainMapper() }
  factory { PictureToEntityMapper(get()) }
  factory { PictureEntityToDomainMapper(get()) }
  factory { SizeToEntityMapper() }
  factory { SizeEntityToDomainMapper(get(named(APIVersion.V3))) }
  factory { MediaLocalDataSource(get(), get(), get()) }

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
