package com.raxdenstudios.commons.pagination.ext

import androidx.recyclerview.widget.LinearLayoutManager
import com.raxdenstudios.commons.pagination.model.PageIndex

fun LinearLayoutManager.toPageIndex(): PageIndex =
  PageIndex(childCount + findFirstVisibleItemPosition())
