package com.raxdenstudios.app.movie.di

import com.raxdenstudios.app.movie.data.local.MovieDatabase
import com.raxdenstudios.app.movie.data.local.datasource.MovieLocalDataSource
import com.raxdenstudios.app.movie.data.local.mapper.*
import com.raxdenstudios.app.movie.data.remote.MovieGateway
import com.raxdenstudios.app.movie.data.remote.datasource.MovieRemoteDataSource
import com.raxdenstudios.app.movie.data.remote.mapper.DateDtoToLocalDateMapper
import com.raxdenstudios.app.movie.data.remote.mapper.MovieDtoToDomainMapper
import com.raxdenstudios.app.movie.data.remote.mapper.PictureDtoToDomainMapper
import com.raxdenstudios.app.movie.data.remote.mapper.VoteDtoToDomainMapper
import com.raxdenstudios.app.movie.data.remote.service.MovieV3Service
import com.raxdenstudios.app.movie.data.remote.service.MovieV4Service
import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.app.movie.data.repository.MovieRepositoryImpl
import com.raxdenstudios.app.movie.domain.*
import com.raxdenstudios.app.network.model.APIVersion
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val movieDataModule = module {

  single { MovieDatabase.getInstance(get()) }
  factory { get<MovieDatabase>().watchListDao() }

  factory { MovieToEntityMapper(get(), get()) }
  factory { MovieEntityToDomainMapper(get(), get()) }
  factory { VoteToEntityMapper() }
  factory { VoteEntityToDomainMapper() }
  factory { PictureToEntityMapper(get()) }
  factory { PictureEntityToDomainMapper(get()) }
  factory { SizeToEntityMapper() }
  factory { SizeEntityToDomainMapper(get(named(APIVersion.V3))) }
  factory { MovieLocalDataSource(get(), get(), get()) }

  single { get<Retrofit>(named(APIVersion.V3)).create(MovieV3Service::class.java) }
  single { get<Retrofit>(named(APIVersion.V4)).create(MovieV4Service::class.java) }

  factory { MovieGateway(get(), get(), get()) }

  factory { VoteDtoToDomainMapper() }
  factory { PictureDtoToDomainMapper(get(named(APIVersion.V3))) }
  factory { DateDtoToLocalDateMapper() }
  factory { MovieDtoToDomainMapper(get(), get(), get()) }

  factory { MovieRemoteDataSource(get(), get()) }

  factory<MovieRepository> { MovieRepositoryImpl(get(), get(), get()) }

  factory<AddMovieToWatchListUseCase> { AddMovieToWatchListUseCaseImpl(get()) }
  factory<RemoveMovieFromWatchListUseCase> { RemoveMovieFromWatchListUseCaseImpl(get()) }
  factory<GetMoviesUseCase> { GetMoviesUseCaseImpl(get()) }
}
