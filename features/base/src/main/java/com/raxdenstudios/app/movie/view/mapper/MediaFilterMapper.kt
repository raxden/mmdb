package com.raxdenstudios.app.movie.view.mapper

import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.commons.util.DataMapper

class MediaFilterToModelMapper(
  private val mediaTypeToModelMapper: MediaTypeToModelMapper
) : DataMapper<MediaFilter, MediaFilterModel>() {

  override fun transform(source: MediaFilter): MediaFilterModel {
    val mediaTypeModel = mediaTypeToModelMapper.transform(source.mediaType)
    return when (source) {
      is MediaFilter.NowPlaying -> MediaFilterModel.NowPlaying(mediaTypeModel)
      is MediaFilter.Popular -> MediaFilterModel.Popular(mediaTypeModel)
      is MediaFilter.TopRated -> MediaFilterModel.TopRated(mediaTypeModel)
      MediaFilter.Upcoming -> MediaFilterModel.Upcoming
      is MediaFilter.WatchList -> MediaFilterModel.WatchList(mediaTypeModel)
    }
  }
}

class MediaFilterModelToDomainMapper(
  private val mediaTypeModelToDomainMapper: MediaTypeModelToDomainMapper
) : DataMapper<MediaFilterModel, MediaFilter>() {

  override fun transform(source: MediaFilterModel): MediaFilter {
    val mediaType = mediaTypeModelToDomainMapper.transform(source.mediaTypeModel)
    return when (source) {
      is MediaFilterModel.NowPlaying -> MediaFilter.NowPlaying(mediaType)
      is MediaFilterModel.Popular -> MediaFilter.Popular(mediaType)
      is MediaFilterModel.TopRated -> MediaFilter.TopRated(mediaType)
      MediaFilterModel.Upcoming -> MediaFilter.Upcoming
      is MediaFilterModel.WatchList -> MediaFilter.WatchList(mediaType)
    }
  }
}