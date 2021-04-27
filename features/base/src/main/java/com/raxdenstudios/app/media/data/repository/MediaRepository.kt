package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

  fun watchList(mediaType: MediaType): Flow<ResultData<List<Media>>>
  fun observeWatchList(mediaType: MediaType): Flow<ResultData<List<Media>>>
  suspend fun addToWatchList(mediaId: Long, mediaType: MediaType): ResultData<Media>
  suspend fun addToLocalWatchList(medias: List<Media>): ResultData<Boolean>
  suspend fun removeFromWatchList(mediaId: Long, mediaType: MediaType): ResultData<Boolean>

  suspend fun medias(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>>
}
