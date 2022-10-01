package com.raxdenstudios.app.core.data.local.mapper

import com.raxdenstudios.app.core.database.model.WatchlistEntity
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class MediaToWatchlistEntityMapper @Inject constructor() :
    DataMapper<Media, WatchlistEntity>() {

    override fun transform(source: Media): WatchlistEntity = WatchlistEntity(source.id.value)
}
