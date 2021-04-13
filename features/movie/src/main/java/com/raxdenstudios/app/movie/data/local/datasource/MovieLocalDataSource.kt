package com.raxdenstudios.app.movie.data.local.datasource

import com.raxdenstudios.app.movie.data.local.MovieDao
import com.raxdenstudios.app.movie.data.local.mapper.MovieEntityToDomainMapper
import com.raxdenstudios.app.movie.data.local.mapper.MovieToEntityMapper
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class MovieLocalDataSource(
  private val dao: MovieDao,
  private val movieToEntityMapper: MovieToEntityMapper,
  private val movieEntityToDomainMapper: MovieEntityToDomainMapper,
) {

  suspend fun watchList(page: Page, pageSize: PageSize): PageList<Movie> {
    val startIndex = page.value - 1 * pageSize.value
    val endIndex = startIndex + pageSize.value
    val dtoList = dao.watchList()
    return try {
      val dtoPageList = dtoList.subList(startIndex, endIndex)
      val movies = movieEntityToDomainMapper.transform(dtoPageList)
      PageList(movies, page)
    } catch (e: Throwable) {
      PageList(emptyList(), page)
    }
  }

  suspend fun insert(movies: List<Movie>) {
    val entityList = movieToEntityMapper.transform(movies)
    dao.insert(entityList)
  }

  suspend fun insert(movie: Movie) {
    val entity = movieToEntityMapper.transform(movie)
    dao.insert(entity)
  }

  suspend fun isWatchList(movieId: Long): Boolean =
    dao.find(movieId)?.watchList ?: false
}