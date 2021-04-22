package com.raxdenstudios.app.network.model

import com.google.gson.annotations.Expose
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

data class PageDto<T : Any>(
  @Expose val page: Int,
  @Expose val total_pages: Int,
  @Expose val total_results: Int,
  @Expose val results: List<T>,
) {

  inline fun <R> toPageList(transform: (List<T>) -> List<R>): PageList<R> =
    PageList(
      items = transform(results),
      page = Page(page)
    )

  companion object {
    val empty = PageDto(
      page = 0,
      total_pages = 0,
      total_results = 0,
      results = emptyList()
    )
  }
}
