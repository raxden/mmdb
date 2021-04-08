package com.raxdenstudios.commons.pagination.model

data class PageSize(val value: Int) {

  companion object {
    val defaultSize = PageSize(10)
  }
}
