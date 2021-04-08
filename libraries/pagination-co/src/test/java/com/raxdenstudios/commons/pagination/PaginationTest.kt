package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Test

@ExperimentalCoroutinesApi
internal class PaginationTest {

  private val configuration = Pagination.Config.default

  private val pageRequest: (page: Page, pageSize: PageSize) -> ResultData<PageList<String>> =
    mockk()
  private val pageResponse: (result: PageResult<String>) -> PageResult<String> =
    mockk(relaxed = true)

  private val testCoroutineScope = TestCoroutineScope()
  private val pagination: Pagination<String> by lazy {
    Pagination(
      config = configuration,
      coroutineScope = testCoroutineScope
    )
  }

  @Test
  fun `Given a first page with results, When requestPage is called, Then a Loading and Content states are returned`() {
    givenAPageWithResults(Page(0))

    requestPage(PageIndex.first)

    coVerifyOrder {
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems))
    }
    confirmVerified(pageResponse)
  }

  @Test
  fun `Given a first page with incomplete results (less than 10), When requestPage is called several times, Then loading, content and noMoreResults states are returned`() {
    givenAPageWithIncompleteResults(Page(0))

    for (index in PageIndex.first.value..10) requestPage(PageIndex(index))

    coVerifyOrder {
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aIncompletePageItems))
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
    }
    confirmVerified(pageResponse)
  }

  @Test
  fun `Given a first page, When requestPage is called several times, Then loading, content states are returned for every page`() {
    givenAPageWithResults(Page(0))
    givenAPageWithResults(Page(1))
    givenAPageWithResults(Page(2))
    givenAPageWithResults(Page(3))
    givenAPageWithResults(Page(4))
    givenAPageWithResults(Page(5))

    for (index in PageIndex.first.value..48) requestPage(PageIndex(index))

    coVerifyOrder {
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems + aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems + aPageItems + aPageItems + aPageItems))
    }
    confirmVerified(pageResponse)
  }

  private fun requestPage(pageIndex: PageIndex) {
    pagination.requestPage(
      pageIndex = pageIndex,
      pageRequest = { page, pageSize -> pageRequest(page, pageSize) },
      pageResponse = { pageResult -> pageResponse(pageResult) }
    )
  }

  private fun givenAPageWithResults(page: Page) {
    coEvery { pageRequest.invoke(page, PageSize.defaultSize) } returns ResultData.Success(
      PageList(aPageItems, page)
    )
  }

  private fun givenAPageWithIncompleteResults(page: Page) {
    coEvery { pageRequest.invoke(page, PageSize.defaultSize) } returns ResultData.Success(
      PageList(aIncompletePageItems, page)
    )
  }
}

private val aIncompletePageItems = listOf("item_1", "item_2", "item_3", "item_4")
private val aPageItems = listOf(
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item"
)
