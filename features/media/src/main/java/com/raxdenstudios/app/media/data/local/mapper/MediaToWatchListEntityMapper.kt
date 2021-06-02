package com.raxdenstudios.app.media.data.local.mapper

import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.util.DataMapper

internal class MediaToWatchListEntityMapper : DataMapper<Media, WatchListEntity>() {
  override fun transform(source: Media): WatchListEntity = WatchListEntity(source.id.value)
}
