package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.movie.data.local.datasource.MovieLocalDataSource
import com.raxdenstudios.app.movie.data.remote.datasource.MovieRemoteDataSource
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.getValueOrNull
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class MovieRepositoryImpl(
  private val movieRemoteDataSource: MovieRemoteDataSource,
  private val movieLocalDataSource: MovieLocalDataSource,
  private val accountLocalDataSource: AccountLocalDataSource,
) : MovieRepository {

  override suspend fun addMovieToWatchList(movieId: Long): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> addMovieToWatchList(account.credentials.accountId, movieId)
    }

  private suspend fun addMovieToWatchList(accountId: String, movieId: Long): ResultData<Boolean> =
    when (val result = movieRemoteDataSource.addMovieToWatchList(accountId, movieId)) {
      is ResultData.Error -> result
      is ResultData.Success -> markMovieAsWatchedAndPersistInLocal(movieId)
    }

  private suspend fun markMovieAsWatchedAndPersistInLocal(movieId: Long): ResultData<Boolean> =
    when (val result = movieRemoteDataSource.detail(movieId)) {
      is ResultData.Error -> result
      is ResultData.Success -> {
        val movie = result.value.copy(watchList = true)
        movieLocalDataSource.insert(movie)
        ResultData.Success(true)
      }
    }

  override suspend fun removeMovieFromWatchList(movieId: Long): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> removeMovieFromWatchList(account.credentials.accountId, movieId)
    }

  private suspend fun removeMovieFromWatchList(
    accountId: String,
    movieId: Long
  ): ResultData<Boolean> =
    when (val result = movieRemoteDataSource.removeMovieFromWatchList(accountId, movieId)) {
      is ResultData.Error -> result
      is ResultData.Success -> markMovieAsNotWatchedAndPersistInLocal(movieId)
    }

  private suspend fun markMovieAsNotWatchedAndPersistInLocal(movieId: Long): ResultData<Boolean> =
    when (val result = movieRemoteDataSource.detail(movieId)) {
      is ResultData.Error -> result
      is ResultData.Success -> {
        val movie = result.value.copy(watchList = false)
        movieLocalDataSource.insert(movie)
        ResultData.Success(true)
      }
    }

  override suspend fun movies(
    searchType: SearchType,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Movie>> {
    return when (val result = movieRemoteDataSource.movies(searchType, page)) {
      is ResultData.Error -> result
      is ResultData.Success -> markMoviesAsWatchedIfWereWatched(result.value)
    }
  }

  private suspend fun markMoviesAsWatchedIfWereWatched(
    pageList: PageList<Movie>
  ): ResultData<PageList<Movie>> =
    ResultData.Success(
      pageList.copy(
        items = pageList.items.map { movie ->
          movie.copy(watchList = movieLocalDataSource.isWatchList(movie.id))
        }
      )
    )

  override suspend fun loadWatchListFromRemoteAndPersistInLocal() {
    val result = when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.watchList(account.credentials.accountId)
    }
    result.getValueOrNull()?.let { movies -> movieLocalDataSource.insert(movies) }
  }

  override suspend fun watchList(page: Page, pageSize: PageSize): ResultData<PageList<Movie>> {
    val pageList = movieLocalDataSource.watchList(page, pageSize)
    return if (pageList.items.isEmpty()) {
      when (val result = watchListFromRemote(page, pageSize)) {
        is ResultData.Error -> result
        is ResultData.Success -> result.also { movieLocalDataSource.insert(result.value.items) }
      }
    } else ResultData.Success(pageList)
  }

  private suspend fun watchListFromRemote(
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Movie>> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> movieRemoteDataSource.watchList(account.credentials.accountId, page)
    }
}
