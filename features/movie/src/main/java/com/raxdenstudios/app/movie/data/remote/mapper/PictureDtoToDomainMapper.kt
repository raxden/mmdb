package com.raxdenstudios.app.movie.data.remote.mapper

import com.raxdenstudios.app.movie.domain.model.Picture
import com.raxdenstudios.app.movie.domain.model.Size
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.commons.util.DataMapper

internal class PictureDtoToDomainMapper(
  private val apiDataProvider: APIDataProvider,
) : DataMapper<String, Picture>() {

  override fun transform(source: String): Picture = Picture(
    thumbnail = Size.Thumbnail(apiDataProvider.getImageDomain(), source),
    original = Size.Original(apiDataProvider.getImageDomain(), source),
  )
}
