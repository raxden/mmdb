package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Pagination<T>(
  private val config: Config = Config.default,
  private val logger: (message: String) -> Unit = {},
  private val coroutineScope: CoroutineScope
) {

  private val history: MutableMap<Page, List<T>> = mutableMapOf()
  private var currentPage: Page = config.initialPage
  private var itemsLoaded: Int = 0
  private var status: Status = Status.Empty

  fun requestPage(
    pageIndex: PageIndex = PageIndex.first,
    pageRequest: suspend (page: Page, pageSize: PageSize) -> ResultData<PageList<T>>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    if (shouldMakeRequest(pageIndex, pageResponse)) {
      val page = pageIndex.toPage()
      makeRequest(page, pageRequest, pageResponse)
    }
  }

  fun requestPreviousPage(
    pageRequest: suspend (page: Page, pageSize: PageSize) -> ResultData<PageList<T>>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    if (currentPage == config.initialPage) return
    val previousPage = Page(currentPage.value - 1)
    makeRequest(previousPage, pageRequest, pageResponse)
  }

  fun getItemsByPage(page: Page) = history[page] ?: emptyList()

  fun clear() {
    history.clear()
    currentPage = config.initialPage
    itemsLoaded = 0
    status = Status.Empty
  }

  private fun makeRequest(
    page: Page,
    pageRequest: suspend (page: Page, pageSize: PageSize) -> ResultData<PageList<T>>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    processRequestStart(pageResponse)
    coroutineScope.launch {
      when (val resultData = pageRequest.invoke(page, config.pageSize)) {
        is ResultData.Error -> processRequestError(resultData.throwable, pageResponse)
        is ResultData.Success -> processRequestSuccess(resultData.value, pageResponse)
      }
    }
  }

  private fun processRequestError(
    throwable: Throwable,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    status = if (itemsLoadedGreaterThan0()) Status.NotEmpty
    else Status.Empty

    pageResponse(PageResult.Error(throwable))
  }

  private fun itemsLoadedGreaterThan0() = itemsLoaded > 0

  private fun processRequestSuccess(
    pageList: PageList<T>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    itemsLoaded += pageList.totalItems
    status = if (thereAreLessElementsThanPageSize(pageList)) Status.NoMoreResults
    else Status.NotEmpty
    currentPage = pageList.page
    history[pageList.page] = pageList.items

    val items = history.getAllItems()

    val pageResult = if (items.isEmpty()) PageResult.NoResults
    else PageResult.Content(items)

    pageResponse(pageResult)
  }

  private fun MutableMap<Page, List<T>>.getAllItems() = mutableListOf<T>().also { list ->
    toSortedMap(compareBy { page -> page.value }).values.forEach { list.addAll(it) }
  }.toList()

  private fun thereAreLessElementsThanPageSize(pageList: PageList<T>) =
    pageList.totalItems < config.pageSize.value

  private fun processRequestStart(
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    status = Status.Loading

    pageResponse(PageResult.Loading)
  }

  private fun PageIndex.toPage(): Page {
    return if (this == PageIndex.first) config.initialPage
    else {
      val indexWithPrefetchDistance = value + 1 + config.prefetchDistance
      val pageSize = config.pageSize.value
      Page(indexWithPrefetchDistance / pageSize + config.initialPage.value)
    }
  }

  private fun shouldMakeRequest(
    pageIndex: PageIndex,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) = when (status) {
    Status.Empty -> true
    Status.NotEmpty -> indexItsEndOfTheList(pageIndex, itemsLoaded)
    Status.Loading -> false
    Status.NoMoreResults -> {
      pageResponse(PageResult.NoMoreResults)
      false
    }
  }

  private fun indexItsEndOfTheList(pageIndex: PageIndex, itemsLoaded: Int): Boolean {
    val endOfTheList = pageIndex.value >= (itemsLoaded - config.prefetchDistance)
    logger("$endOfTheList <- ${pageIndex.value} >= ($itemsLoaded - ${config.prefetchDistance})")
    return endOfTheList
  }

  private sealed class Status {
    object Empty : Status()
    object NotEmpty : Status()
    object Loading : Status()
    object NoMoreResults : Status()
  }

  data class Config(
    val initialPage: Page,
    val pageSize: PageSize,
    val prefetchDistance: Int,
  ) {

    companion object {
      val default = Config(
        initialPage = Page(0),
        pageSize = PageSize.defaultSize,
        prefetchDistance = 2
      )
    }
  }
}
