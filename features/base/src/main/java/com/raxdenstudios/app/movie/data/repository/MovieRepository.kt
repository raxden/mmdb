package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

interface MovieRepository {
  suspend fun addMovieToWatchList(movieId: Long): ResultData<Boolean>
  suspend fun removeMovieFromWatchList(movieId: Long): ResultData<Boolean>
  suspend fun movies(searchType: SearchType, page: Page, pageSize: PageSize): ResultData<PageList<Movie>>
  suspend fun watchList(page: Page, pageSize: PageSize): ResultData<PageList<Movie>>
}
