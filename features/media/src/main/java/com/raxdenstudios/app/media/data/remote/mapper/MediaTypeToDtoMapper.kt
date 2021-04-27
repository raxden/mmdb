package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.util.DataMapper

internal class MediaTypeToDtoMapper : DataMapper<MediaType, String>() {

  override fun transform(source: MediaType): String = when (source) {
    MediaType.MOVIE -> "movie"
    MediaType.TV_SHOW -> "tv"
    else -> throw IllegalStateException("MediaType doesn't allowed")
  }
}
