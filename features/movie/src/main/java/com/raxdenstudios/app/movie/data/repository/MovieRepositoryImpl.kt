package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.movie.data.remote.datasource.MovieRemoteDataSource
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.app.movie.domain.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class MovieRepositoryImpl(
  private val movieRemoteDataSource: MovieRemoteDataSource,
  private val accountLocalDataSource: AccountLocalDataSource,
) : MovieRepository {

  override suspend fun addMovieToWatchList(movieId: Long): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.addMovieToWatchList(
        account.credentials.accountId,
        movieId
      )
    }

  override suspend fun removeMovieFromWatchList(movieId: Long): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.removeMovieFromWatchList(
        account.credentials.accountId,
        movieId
      )
    }

  override suspend fun movies(searchType: SearchType, page: Page): ResultData<PageList<Movie>> =
    movieRemoteDataSource.movies(searchType, page)

  override suspend fun watchList(page: Page): ResultData<PageList<Movie>> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.watchList(account.credentials.accountId, page)
    }
}
