package com.raxdenstudios.app.movie.di

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
import com.raxdenstudios.app.movie.domain.AddMovieToWatchList
import com.raxdenstudios.app.movie.domain.AddMovieToWatchListImpl
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchList
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchListImpl
import com.raxdenstudios.app.network.model.APIVersion
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val movieDataModule = module {

  single { get<Retrofit>(named(APIVersion.V3)).create(MovieV3Service::class.java) }
  single { get<Retrofit>(named(APIVersion.V4)).create(MovieV4Service::class.java) }

  factory { MovieGateway(get(), get()) }

  factory { VoteDtoToDomainMapper() }
  factory { PictureDtoToDomainMapper(get(named(APIVersion.V3))) }
  factory { DateDtoToLocalDateMapper() }
  factory { MovieDtoToDomainMapper(get(), get(), get()) }

  factory { MovieRemoteDataSource(get(), get()) }

  factory<MovieRepository> { MovieRepositoryImpl(get(), get()) }

  factory<AddMovieToWatchList> { AddMovieToWatchListImpl(get()) }
  factory<RemoveMovieFromWatchList> { RemoveMovieFromWatchListImpl(get()) }
}
