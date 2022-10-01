package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class MediaTypeToDtoMapper @Inject constructor() : DataMapper<MediaType, String>() {

    override fun transform(source: MediaType): String = when (source) {
        MediaType.Movie -> "movie"
        MediaType.TvShow -> "tv"
        else -> error("MediaType doesn't allowed")
    }
}
