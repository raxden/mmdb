package com.raxdenstudios.app.movie.data.repository

import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

interface MediaRepository {
  suspend fun addMediaToWatchList(mediaId: Long, mediaType: MediaType): ResultData<Boolean>
  suspend fun removeMediaFromWatchList(mediaId: Long, mediaType: MediaType): ResultData<Boolean>
  suspend fun medias(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>>

  suspend fun loadWatchListInLocal(mediaType: MediaType): ResultData<Boolean>
}
