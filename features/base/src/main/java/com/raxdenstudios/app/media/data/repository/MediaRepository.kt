package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

interface MediaRepository {
  suspend fun addMediaToWatchList(mediaId: Long, mediaType: MediaType): ResultData<Media>
  suspend fun removeMediaFromWatchList(mediaId: Long, mediaType: MediaType): ResultData<Boolean>
  suspend fun medias(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>>

  suspend fun loadWatchListInLocal(mediaType: MediaType): ResultData<Boolean>
}
