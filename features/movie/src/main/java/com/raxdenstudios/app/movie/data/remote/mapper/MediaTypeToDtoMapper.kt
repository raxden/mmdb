package com.raxdenstudios.app.movie.data.remote.mapper

import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.commons.util.DataMapper

internal class MediaTypeToDtoMapper : DataMapper<MediaType, String>() {

  override fun transform(source: MediaType): String = when (source) {
    MediaType.Movie -> "movie"
    MediaType.TVShow -> "tv"
  }
}
