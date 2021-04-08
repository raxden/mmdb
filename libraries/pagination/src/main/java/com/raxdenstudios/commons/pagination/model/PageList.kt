package com.raxdenstudios.commons.pagination.model

data class PageList<T>(
  val items: List<T>,
  val page: Page
) {

  val totalItems: Int
    get() = items.size

  fun <R> map(mapper: (List<T>) -> List<R>): PageList<R> = PageList(
    items = mapper(items),
    page = page
  )
}
