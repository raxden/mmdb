package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.movie.data.local.datasource.MovieLocalDataSource
import com.raxdenstudios.app.movie.data.remote.datasource.MovieRemoteDataSource
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.coFlatMap
import com.raxdenstudios.commons.coMap
import com.raxdenstudios.commons.map
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
    movieRemoteDataSource.addMovieToWatchList(accountId, movieId)
      .coFlatMap { getMovieAndMarkAsWatched(movieId, true) }
      .coFlatMap { movie -> updateMovie(movie) }

  override suspend fun removeMovieFromWatchList(movieId: Long): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> removeMovieFromWatchList(account.credentials.accountId, movieId)
    }

  private suspend fun removeMovieFromWatchList(
    accountId: String,
    movieId: Long
  ): ResultData<Boolean> =
    movieRemoteDataSource.removeMovieFromWatchList(accountId, movieId)
      .coFlatMap { getMovieAndMarkAsWatched(movieId, false) }
      .coFlatMap { movie -> updateMovie(movie) }

  private suspend fun getMovieAndMarkAsWatched(movieId: Long, watched: Boolean) =
    movieRemoteDataSource.movieById(movieId).map { movie -> movie.copy(watchList = watched) }

  private suspend fun updateMovie(movie: Movie): ResultData.Success<Boolean> {
    movieLocalDataSource.insert(movie)
    return ResultData.Success(true)
  }

  override suspend fun movies(
    searchType: SearchType,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Movie>> =
    movieRemoteDataSource.movies(searchType, page)
      .coMap { pageList -> markMoviesAsWatchedIfWereWatched(pageList) }

  private suspend fun markMoviesAsWatchedIfWereWatched(pageList: PageList<Movie>) =
    pageList.copy(
      items = pageList.items.map { movie ->
        movie.copy(watchList = movieLocalDataSource.isWatchList(movie.id))
      }
    )

  override suspend fun loadWatchListFromRemoteAndPersistInLocal(): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> loadWatchListFromRemoteAndPersistInLocal(account.credentials.accountId)
    }

  private suspend fun loadWatchListFromRemoteAndPersistInLocal(
    accountId: String
  ): ResultData<Boolean> =
    movieRemoteDataSource.watchList(accountId)
      .coFlatMap { movies -> updateMovies(movies) }

  private suspend fun updateMovies(movies: List<Movie>): ResultData.Success<Boolean> {
    movieLocalDataSource.insert(movies)
    return ResultData.Success(true)
  }

  override suspend fun watchList(page: Page, pageSize: PageSize): ResultData<PageList<Movie>> {
    val pageList = movieLocalDataSource.watchList(page, pageSize)
    return ResultData.Success(pageList)
  }
}
