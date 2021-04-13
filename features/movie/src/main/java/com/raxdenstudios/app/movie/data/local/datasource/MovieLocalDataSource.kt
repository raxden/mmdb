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

  suspend fun watchList(): List<Movie> {
    val entityList = dao.watchList()
    return movieEntityToDomainMapper.transform(entityList)
  }

  suspend fun watchList(page: Page, pageSize: PageSize): PageList<Movie> {
    val dtoList = dao.watchList()
    val startIndex = (page.value - 1) * pageSize.value
    val endIndex = when {
      startIndex + pageSize.value > dtoList.size -> dtoList.size
      else -> startIndex + pageSize.value
    }
    return try {
      val dtoPageList = dtoList.subList(startIndex, endIndex)
      val movies = movieEntityToDomainMapper.transform(dtoPageList)
      PageList(movies, page)
    } catch (throwable: Throwable) {
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