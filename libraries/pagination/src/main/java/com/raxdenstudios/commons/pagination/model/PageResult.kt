package com.raxdenstudios.commons.pagination.model

sealed class PageResult<out T> {
  object Loading : PageResult<Nothing>()
  data class Content<T>(val items: List<T>) : PageResult<T>()
  data class Error(val throwable: Throwable) : PageResult<Nothing>()
  object NoResults : PageResult<Nothing>()
  object NoMoreResults : PageResult<Nothing>()
}
