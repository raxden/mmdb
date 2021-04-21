package com.raxdenstudios.app.media.view.mapper

import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaTypeModel
import com.raxdenstudios.commons.util.DataMapper

class MediaTypeToModelMapper : DataMapper<MediaType, MediaTypeModel>() {

  override fun transform(source: MediaType): MediaTypeModel = when (source) {
    MediaType.Movie -> MediaTypeModel.Movie
    MediaType.TVShow -> MediaTypeModel.TVShow
  }
}

class MediaTypeModelToDomainMapper : DataMapper<MediaTypeModel, MediaType>() {

  override fun transform(source: MediaTypeModel): MediaType = when (source) {
    MediaTypeModel.Movie -> MediaType.Movie
    MediaTypeModel.TVShow -> MediaType.TVShow
  }
}