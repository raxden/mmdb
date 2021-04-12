package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.movie.data.local.datasource.MovieWatchListLocalDataSource
import com.raxdenstudios.app.movie.data.remote.datasource.MovieRemoteDataSource
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.app.movie.domain.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class MovieRepositoryImpl(
  private val movieRemoteDataSource: MovieRemoteDataSource,
  private val movieWatchListLocalDataSource: MovieWatchListLocalDataSource,
  private val accountLocalDataSource: AccountLocalDataSource,
) : MovieRepository {

  override suspend fun addMovieToWatchList(movieId: Long): ResultData<Boolean> {
    val result = when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.addMovieToWatchList(
        account.credentials.accountId,
        movieId
      )
    }
    return addMovieAsWatchedIfSuccess(result, movieId)
  }

  private suspend fun addMovieAsWatchedIfSuccess(
    result: ResultData<Boolean>,
    movieId: Long
  ) = when (result) {
    is ResultData.Error -> result
    is ResultData.Success -> result.also {
      movieWatchListLocalDataSource.insert(Movie.withId(movieId))
    }
  }

  override suspend fun removeMovieFromWatchList(movieId: Long): ResultData<Boolean> {
    val result = when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.removeMovieFromWatchList(
        account.credentials.accountId,
        movieId
      )
    }
    return removeMovieFromWatchListIfSuccess(result, movieId)
  }

  private suspend fun removeMovieFromWatchListIfSuccess(
    result: ResultData<Boolean>,
    movieId: Long
  ) = when (result) {
    is ResultData.Error -> result
    is ResultData.Success -> {
      movieWatchListLocalDataSource.remove(Movie.withId(movieId))
      result
    }
  }

  override suspend fun movies(searchType: SearchType, page: Page): ResultData<PageList<Movie>> {
    return when (val result = movieRemoteDataSource.movies(searchType, page)) {
      is ResultData.Error -> result
      is ResultData.Success -> ResultData.Success(markMoviesAsWatchedIfWereWatched(result))
    }
  }

  private suspend fun markMoviesAsWatchedIfWereWatched(
    result: ResultData.Success<PageList<Movie>>
  ): PageList<Movie> = result.value.copy(
    items = result.value.items.map { movie ->
      movie.copy(watchList = movieWatchListLocalDataSource.contains(movie.id))
    }
  )

  override suspend fun watchList(page: Page): ResultData<PageList<Movie>> {
    val result = when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.watchList(account.credentials.accountId, page)
    }
    return insertMoviesIntoWatchListIfSuccess(result)
  }

  private suspend fun insertMoviesIntoWatchListIfSuccess(result: ResultData<PageList<Movie>>) =
    when (result) {
      is ResultData.Error -> result
      is ResultData.Success -> result.also {
        movieWatchListLocalDataSource.insert(result.value.items)
      }
    }
}
