package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

internal class MediaTypeToDtoMapper @Inject constructor() : DataMapper<MediaType, String>() {

    override fun transform(source: MediaType): String = when (source) {
        MediaType.MOVIE -> "movie"
        MediaType.TV_SHOW -> "tv"
        else -> throw IllegalStateException("MediaType doesn't allowed")
    }
}
