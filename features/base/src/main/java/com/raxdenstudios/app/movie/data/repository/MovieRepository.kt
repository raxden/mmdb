package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.app.movie.domain.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

interface MovieRepository {
  suspend fun addMovieToWatchList(movieId: Long): ResultData<Boolean>
  suspend fun removeMovieFromWatchList(movieId: Long): ResultData<Boolean>
  suspend fun movies(searchType: SearchType, page: Page): ResultData<PageList<Movie>>
  suspend fun watchList(page: Page): ResultData<PageList<Movie>>
}
